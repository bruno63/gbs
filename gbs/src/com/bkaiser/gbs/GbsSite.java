package com.bkaiser.gbs;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import javax.imageio.ImageIO;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;

import org.jdom2.*;

/**
 * This class represents a GBS Site and provides all
 * file-relevant methods. 
 * 
 * @author Bruno Kaiser
 */
public class GbsSite  {
	private static final String CN = "GbsSite";
	private Element siteDescEl = null;   // the site description
	private File srcDir = null;
	private File destDir = null;
	private GbsTemplate templatePage = null;

	public GbsSite(Element siteDesc, File srcD, File destD) 
	{
		srcDir = srcD;
		destDir = destD;
		siteDescEl = siteDesc; 
	}

	public void createTemplate() throws JDOMException, IOException
	{
		// todo: check whether title is set in every page.
		// add the navigation to the destination template file
		GbsNavigation _nav = new GbsNavigation(siteDescEl, NavigationType.NAVBAR);
		_nav.setInversion(false);
		templatePage = new GbsTemplate();
		templatePage.addNavigation(_nav);

		// add the footer to the destination template file
		// first, try to use the child element footer
		Element _footerEl = siteDescEl.getChild("footer");
		// if there is no child element footer, use attribute footerText or default
		if (_footerEl == null) {
			templatePage.addFooter(GbsPage.getFooter(siteDescEl.getAttributeValue("footerText"), true));
		}
		else {
			templatePage.addFooter(_footerEl);
		}
	}

	public void generatePages() throws JDOMException, IOException
	{
		generatePages(siteDescEl.getChildren("node"));
	}

	private void generatePages(List<Element> nodes) throws JDOMException, IOException 
	{
		String _nodeType = null;
		Element _nodeEl = null;
		GbsPage _page = null;

		for (int i = 0; i < nodes.size(); i++) {
			_nodeEl = (Element) nodes.get(i);
			_nodeType = _nodeEl.getAttributeValue("type").toLowerCase();

			if (_nodeType.equals("jumbotron")) {
				copyAllImages(_nodeEl);
				_page = new GbsJumbotron(templatePage);
				((GbsJumbotron) _page).setClearFixed();
				_page.preparePage(_nodeEl);
				_page.savePage(new File(destDir, _nodeEl.getAttributeValue("url")));
			}
			else if (_nodeType.equals("jumbologin")) {
				copyImages(_nodeEl.getChild("heroUnit").getChildren("image"));
				_page = new GbsJumboLogin(templatePage);
				addBackgroundImages((GbsJumboLogin) _page, _nodeEl.getChild("heroUnit").getChildren("image"));
				_page.preparePage(_nodeEl);
				_page.savePage(new File(destDir, _nodeEl.getAttributeValue("url")));
			}
			else if (_nodeType.equals("menu")) {
				generatePages(_nodeEl.getChildren("node"));
			}
			else if (_nodeType.equals("gallery")) {
				File[] _files = copyGallery(_nodeEl.getAttributeValue("resDir"), "jpg");
				_page = new GbsGallery(templatePage, _files);
				_page.preparePage(_nodeEl);
				_page.savePage(new File(destDir, _nodeEl.getAttributeValue("url")));
			}
			else if (_nodeType.equals("iconindex")) {
				copyAllImages(_nodeEl);
				_page = new GbsIconIndex(templatePage);
				_page.preparePage(_nodeEl);
				_page.savePage(new File(destDir, _nodeEl.getAttributeValue("url")));
	
			}
			else if (_nodeType.equals("doclist")) {
				File[] _files = copyFiles(_nodeEl.getAttributeValue("resDir"), "pdf");
				_page = new  GbsDocList(templatePage, _files);
				_page.preparePage(_nodeEl);
				_page.savePage(new File(destDir, _nodeEl.getAttributeValue("url")));
			}
			else if (_nodeType.equals("link")) {
				String _fn = _nodeEl.getAttributeValue("url");
				if (_fn.startsWith("http")) {
					// no page needs to be created
					// the menu item links directly to the external page
				}
				else {
					// we do not want orphaned links. Therefore, we generate
					// empty template pages if the page not yet exists
					File _f = new File(destDir, _nodeEl.getAttributeValue("url"));
					if (! _f.exists()) {
						_page = new GbsSimplePage(templatePage);
						_page.preparePage(_nodeEl);
						_page.savePage(new File(destDir, _nodeEl.getAttributeValue("url")));
					}					
				}
			}
			else if (_nodeType.equals("carousel")) {
				copyImages(_nodeEl.getChildren("image"));
				_page = new GbsCarousel(templatePage);
				_page.preparePage(_nodeEl);
				_page.savePage(new File(destDir, _nodeEl.getAttributeValue("url")));
			}
			else {
				GbsLogUtil.throwFatal(CN, "generatePages", "type " + _nodeType + " is unknown");
			}
			// generate all pages recursively
			generatePages(_nodeEl.getChildren("node"));
		}
	}


	public static void generateSlideImage(File originalF, File destF, double scalingFactor) throws IOException
	{
		BufferedImage _originalImg = ImageIO.read(originalF);
		BufferedImage _slideImg = Thumbnails.of(_originalImg)
				.scale(scalingFactor)
				.asBufferedImage();
		ImageIO.write(_slideImg, "jpg", destF);

	}

	public static void generateThumbnailImage(File originalF, File destF, int height, int width) throws IOException {
		BufferedImage _originalImg = ImageIO.read(originalF);
		BufferedImage _thumbnailImg = Thumbnails.of(_originalImg)
				.size(height, width)
				.crop(Positions.CENTER)
				.asBufferedImage();
		ImageIO.write(_thumbnailImg, "jpg", destF);
	}

	public void copyImage(GbsImage image) throws IOException
	{
		// destDir.mkdirs();
		String _srcDirPath = new File(srcDir, image.getRelativePath()).getAbsolutePath();
		File _destImg = new File(destDir, image.getRelativePath());
		_destImg.mkdirs();
		String _destDirPath = _destImg.getAbsolutePath();
		Files.copy(Paths.get(_srcDirPath), Paths.get(_destDirPath), REPLACE_EXISTING);
	}
	
	private File[] copyGallery(String resDir, String filter) throws IOException
	{	
		File _slideImg = null;
		File _thumbImg = null;
		destDir.mkdir();
		File _destThumbDir = new File(destDir, resDir + File.separator + "thumbs");
		_destThumbDir.mkdirs();
		
		File[] _images = selectFiles(new File(srcDir, resDir), filter);
		for (int i = 0; i < _images.length; i++) {
			_slideImg = new File(destDir, resDir + File.separator + _images[i].getName());
			_thumbImg = new File(_destThumbDir, _images[i].getName());
			if (! _slideImg.exists()) { // generate slide image
				// TODO: only scale if the images is larger than a certain size
				generateSlideImage(_images[i], _slideImg, 0.5f); 
			}
			if (! _thumbImg.exists()) {  // generate thumbnail image
				generateThumbnailImage(_images[i], _thumbImg, 200, 200); 
			}
		}
		return _images;
	}
	
	private void copyImages(List<Element> images) throws IOException
	{
		for (int i = 0; i < images.size(); i++) {
			copyImage(new GbsImage(images.get(i)));
		}
	}
	
	// todo: solve this easier with a xpath query //image
	private void copyAllImages(Element el) throws IOException
	{
		copyImages(el.getChildren("image", templatePage.getNamespace()));
		List<Element> _elems = el.getChildren();
		for (int i = 0; i < _elems.size(); i++) {
			copyAllImages(_elems.get(i));
		}
	}
	
	private void addBackgroundImages(GbsJumboLogin page, List<Element> images)
	{
		for (int i = 0; i < images.size(); i++) {
			page.addBackgroundImage(new File(images.get(i).getAttributeValue("src")));
		}
	}

	private void copyFile(String relPath) throws IOException
	{
		destDir.mkdirs();
		String _srcDirPath = new File(srcDir, relPath).getAbsolutePath();
		String _destDirPath = new File(destDir, relPath).getAbsolutePath();
		Files.copy(Paths.get(_srcDirPath), Paths.get(_destDirPath), REPLACE_EXISTING);
	}
	
	private File[] copyFiles(String resDir, String filter) throws IOException
	{
		File _srcDocDir = new File(srcDir, resDir);
		File[] _files = selectFiles(_srcDocDir, filter);
		for (int i = 0; i < _files.length; i++) {
			// move the file into the destination directory
			copyFile(resDir + File.separator + _files[i].getName());
		}
		return _files;
	}

	/**
	 * Filters all files within directory dir according to a file extension.
	 * @param   dir         the current directory to look for the files
	 * @param   extension   the file name extension is the selection criteria
	 * @return	an array of pdf files
	 */
	public static File[] selectFiles(File dir, String extension) {
		try {
			FilenameFilter _filter = new GbsFileFilter(extension);
			return (dir.listFiles(_filter));
		}
		catch (Exception _ex) {
			GbsLogUtil.throwFatal(CN, "selectFiles", _ex.toString());
			return null;
		}
	}
}
