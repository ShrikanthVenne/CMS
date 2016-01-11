package com.funongo.cms;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.funongo.cms.bo.ContentBO;
import com.funongo.cms.bo.FileBO;
import com.funongo.cms.bo.UploadBO;
import com.funongo.cms.service.ImportService;

@Controller
public class ExcelImportController {
	
	@Autowired
	ImportService importService;
	
	@RequestMapping(value="/bulkUpload", method=RequestMethod.GET)
	public String upload(){		
		return "uploadContent";		
	}
	
	 @RequestMapping(value="doUpload", method = RequestMethod.POST)
	    public String doUpload(@ModelAttribute("fileBO") FileBO fileBO, Model map, RedirectAttributes redirectAttributes) {
	        //importService.import(uploadItem);
		    //FileBO fileBO = (FileBO)command;
		 	System.out.println("filee..");
		 	System.out.println(fileBO.getFile());
		 	String fileName = fileBO.getFile().getOriginalFilename();
		 	//System.out.println();
		 	if(fileName.endsWith(".xls") || fileName.endsWith(".xlsx")){
		 		UploadBO uploadBO = importService.importData(fileBO);
			 	HashMap<String, ArrayList<String>> errors = uploadBO.getErrors();
			 	if(errors.size() > 0){
			 		redirectAttributes.addFlashAttribute("messages", errors);
			 		return "redirect:bulkUpload";
			 	}
			 	else{
			 		ArrayList<ContentBO> contentList = uploadBO.getContentList();
			 		redirectAttributes.addFlashAttribute("contentList", contentList);
			 		return "redirect:bulkUpload";
			 	}
		 	}
		 	else{
		 		redirectAttributes.addFlashAttribute("error", "Uploaded file isn't a excel file");
		 		return "redirect:bulkUpload";
		 	}
		 	
	       
	    }
	 
		@RequestMapping(value="/viewContent", method=RequestMethod.GET)
		public String viewContent(){			
			return "viewContent";		
		}

}
