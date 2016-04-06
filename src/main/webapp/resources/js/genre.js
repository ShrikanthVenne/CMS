 var selectedCategories = [];
	$(document).ready(function(){
		$('#categories').multiselect({ 
	         includeSelectAllOption: true,
	         enableFiltering:true,        
	         enableCaseInsensitiveFiltering:true	        	         
	     });
		
		 $("#categories").multiselect('selectAll', false);
		 $("#categories").multiselect('updateButtonText');
		 getGenres();
		
	});	

	function getGenres(){				
		if($('#categories').val() != null){
			$('#categoryLabel').hide();
			 $.ajax({
				 url: contextPath+"/getGenreFromCategory",
				 data:{
					 categories:$('#categories').val()
				 },
				 success: function(result){
			        $('#genre').html(result);
			        $('#genreTable').dataTable();
			     },
			     error: function(error){
				   
				 }
			 });
		}
		else{
			$('#categoryLabel').show();
		}
		
	}