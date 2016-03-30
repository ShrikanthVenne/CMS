/**
 * 
 */
$(document).ready(function() {
	$("#fromUploadedDateId").datepicker({
		dateFormat : "dd-mm-yy",
		Default : false,
		onClose : function(selectedDate) {
			$("#toUploadedDateId").datepicker("option", "minDate", selectedDate);
		}
	});

	$("#toUploadedDateId").datepicker({
		dateFormat : "dd-mm-yy",
		Default : false,
		minDate : 0,
		onClose : function(selectedDate) {
			$("#fromUploadedDateId").datepicker("option", "maxDate", selectedDate);
		}
	});
	// Reset button functionality
	$("#resetButton").click(function() {
		$("#fromUploadedDateId").val('');
		$("#toUploadedDateId").val('');
		$("#searchText").val('');
	});

	// Get contents functionality
	$("#getContentsButton").click(function() {
		// $body.addClass("loading");
		$(".modal").show();
		var categoryId = $("#categories").val();
		var categoryName = $("#categories :selected").text();
		$.ajax({
			type : "POST",
			url : "getContents",
			data : {
				category_id : categoryId,
				category_name : categoryName,
				search : $("#searchText").val(),
				fromUpload : $("#fromUploadedDateId").val(),
				toUpload : $("#toUploadedDateId").val(),
			},
			success : function(contents) {
				/* $body.removeClass("loading"); */
				$(".modal").hide();
				$("#contentsList").html(contents);
			},
		});

	});
});