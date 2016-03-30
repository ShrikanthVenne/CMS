package com.funongo.cms;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.funongo.cms.bo.CategoryBO;
import com.funongo.cms.bo.Content;
import com.funongo.cms.bo.ContentWrapper;
import com.funongo.cms.bo.Genre;
import com.funongo.cms.bo.TP;
import com.funongo.cms.service.ContentService;

@Controller
public class ContentEditController {
	@Autowired
	ContentService contentService;

	@InitBinder
	public void initListBinder(WebDataBinder dataBinder) {
		// this will allow 500 size of array.
		dataBinder.setAutoGrowCollectionLimit(9999);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		dateFormat.setLenient(false);

		// true passed to CustomDateEditor constructor means convert empty
		// String to null
		dataBinder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}

	@RequestMapping(value = "/editContent", method = RequestMethod.GET)
	public String getEditContentPage(Model model) {
		List<CategoryBO> categoryList = contentService.getCategoryList();
		model.addAttribute("category", categoryList);
		return "editcontent";
	}

	@RequestMapping(value = "/getContents", method = RequestMethod.POST)
	public String getContents(Model model, @ModelAttribute CategoryBO category, @RequestParam("search") String search,
			@RequestParam("fromUpload") String fromDate, @RequestParam("toUpload") String toDate) {
		List<Content> contentList = contentService.getContentList(category, search, fromDate, toDate);
		ContentWrapper wrapper = new ContentWrapper();
		wrapper.setContents(contentList);
		List<Genre> genreList = contentService.getGenreList(category.getCategory_id());
		List<TP> tpList = contentService.getAllTpList();
		List<String> languages = contentService.getLanguages();
		model.addAttribute("tps", tpList);
		model.addAttribute("genres", genreList);
		model.addAttribute("languages", languages);
		model.addAttribute("wrapper", wrapper);
		model.addAttribute("content", new Content());
		return "contentlist";
	}

	@RequestMapping(value = "/getContentForEdit", method = RequestMethod.POST)
	public @ResponseBody Content getContentForEdit(Model model, @RequestParam("contentId") int contentId) {
		Content content = contentService.getContentFromId(contentId);
		return content;
	}

	@RequestMapping(value = "/contentUpdation", method = RequestMethod.POST)
	public String contentUpdation(Model model, @ModelAttribute ContentWrapper contents) {
		Content content = contents.getContents().get(0);
		System.out.println(content.getContentId());
		contentService.updateContent(content);
		model.addAttribute("content", content);
		return "content";
	}
}
