package com.funongo.cms;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.funongo.cms.bo.CategoryBO;
import com.funongo.cms.bo.ContentBO;
import com.funongo.cms.bo.FileBO;
import com.funongo.cms.bo.UploadBO;
import com.funongo.cms.service.ContentService;
import com.funongo.cms.service.ImportService;

@Controller
public class ExcelImportController {

	@Autowired
	ImportService importService;
	
	@Autowired
	ContentService contentService;

	@RequestMapping(value = "/bulkUpload", method = RequestMethod.GET)
	public String upload() {
		return "uploadContent";
	}

	@RequestMapping(value = "/bulkLangUpload", method = RequestMethod.GET)
	public String uploadLangContent() {
		return "uploadlangcontent";
	}

	@RequestMapping(value = "doLangUpload", method = RequestMethod.POST)
	public String doLangUpload(@ModelAttribute("fileBO") FileBO fileBO, Model map,
			RedirectAttributes redirectAttributes) {
		// importService.import(uploadItem);
		// FileBO fileBO = (FileBO)command;
		System.out.println("filee..");
		System.out.println(fileBO.getFile());
		String fileName = fileBO.getFile().getOriginalFilename();

		// System.out.println();
		if (fileName.endsWith(".xls") || fileName.endsWith(".xlsx")) {
			UploadBO uploadBO = importService.importLangData(fileBO);
			HashMap<String, ArrayList<String>> errors = uploadBO.getErrors();
			if (errors.size() > 0) {
				redirectAttributes.addFlashAttribute("messages", errors);
				return "redirect:bulkLangUpload";
			} else {
				redirectAttributes.addFlashAttribute("size", uploadBO.getSize());
				return "redirect:bulkLangUpload";
			} /*
				 * else { ArrayList<ContentBO> contentList =
				 * uploadBO.getContentList();
				 * redirectAttributes.addFlashAttribute("contentList",
				 * contentList); return "redirect:bulkUpload"; }
				 */
		} else {
			redirectAttributes.addFlashAttribute("error", "Uploaded file isn't a excel file");
			return "redirect:bulkLangUpload";
		}

	}

	@RequestMapping(value = "doUpload", method = RequestMethod.POST)
	public String doUpload(@ModelAttribute("fileBO") FileBO fileBO, Model map, RedirectAttributes redirectAttributes) {
		// importService.import(uploadItem);
		// FileBO fileBO = (FileBO)command;
		System.out.println("filee..");
		System.out.println(fileBO.getFile());
		String fileName = fileBO.getFile().getOriginalFilename();
		System.out.println("APP: " + fileBO.isApp());
		System.out.println("WAP: " + fileBO.isWap());

		// System.out.println();
		if (fileName.endsWith(".xls") || fileName.endsWith(".xlsx")) {
			UploadBO uploadBO = importService.importData(fileBO);
			HashMap<String, ArrayList<String>> errors = uploadBO.getErrors();
			if (errors.size() > 0) {
				redirectAttributes.addFlashAttribute("messages", errors);
				return "redirect:bulkUpload";
			} else {
				ArrayList<ContentBO> contentList = uploadBO.getContentList();
				redirectAttributes.addFlashAttribute("contentList", contentList);
				return "redirect:bulkUpload";
			}
		} else {
			redirectAttributes.addFlashAttribute("error", "Uploaded file isn't a excel file");
			return "redirect:bulkUpload";
		}

	}

	@RequestMapping(value = "/viewContent", method = RequestMethod.GET)
	public String viewContent() {
		return "viewContent";
	}
	
	@RequestMapping(value = "/viewCategory", method = RequestMethod.GET)
	public String viewCategory(ModelMap modelMap) {
		ArrayList<CategoryBO> categories = contentService.getAllCategories();
		modelMap.addAttribute("categories", categories);		
		return "viewCategory";
	}
	
	@RequestMapping(value = "/viewGenre", method = RequestMethod.GET)
	public String viewGenre(ModelMap modelMap) {
		ArrayList<CategoryBO> categories = contentService.getAllCategories();
		modelMap.addAttribute("categories", categories);		
		return "viewGenre";
	}

}
