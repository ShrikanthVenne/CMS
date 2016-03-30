$(document).ready(function() {
	$(document).ready(function() {
		$("#contentFeaturingTable").stickyTableHeaders({
			scrollableArea : $(".scrollable-area")[0],
		});
		$("#contentFeaturingTable").tablesorter({
			theme : 'ice',
			dateFormat : "ddmmyyyy",
		});
	});
	var count = $("#listSize").val();

	$("#editContentDialog").dialog({
		autoOpen : false,
		height : 650,
		width : 1100,
		modal : true,
		close : function() {
			$("#editContentDialog").dialog("close");
		}
	});

	var categoryId = $("#categoryIdText").val();
	if (categoryId == 1) {
		$("#releaseDateId").datepicker({
			dateFormat : "dd-mm-yy",
			Default : false,
			minDate : 0,
		});
	}

	$("#updateContentButton").click(function() {

		var categoryId = $("#categoryIdText").val();
		var isError = false;
		if ($("#displayNameId").val() == "") {
			isError = true;
			$("#displayNameError").append("<font color='red'>Display Name cannot be empty</font>");
		}
		if ($("#searchTextId").val() == "") {
			isError = true;
			$("#searchError").append("<font color='red'>Search cannot be empty</font>");
		}
		if ($("#shortDescId").val() == "") {
			isError = true;
			$("#shortDescError").append("<font color='red'>Short Description cannot be empty</font>");
		}
		if ($("#longDescId").val() == "") {
			isError = true;
			$("#longDescError").append("<font color='red'>Long Description cannot be empty</font>");
		}

		if ($("#ratingId").val() != "") {
			if (!$.isNumeric($("#ratingId").val())) {
				isError = true;
				$("#ratingError").append("<font color='red'>Rating should be numeric</font>");
			} else if ($("#ratingId").val() > 5) {
				isError = true;
				$("#ratingError").append("<font color='red'>Rating should be between 0 and 5</font>");
			}
		} else {
			isError = true;
			$("#ratingError").append("<font color='red'>Rating cannot be empty</font>");
		}

		if (categoryId == 1 || categoryId == 2 || categoryId == 14) {
			if ($("#smartUrl1Id").val() == "") {
				isError = true;
				$("#smartUrl1Error").append("<font color='red'>Smart Url 1  cannot be empty</font>");
			}
			if ($("#smartUrl2Id").val() == "") {
				isError = true;
				$("#smartUrl2Error").append("<font color='red'>Smart Url 2  cannot be empty</font>");
			}
			if ($("#smartUrl3Id").val() == "") {
				isError = true;
				$("#smartUrl3Error").append("<font color='red'>Smart Url 3 cannot be empty</font>");
			}
		}
		if (categoryId == 1) {
			if ($("#directorsId").val() == "") {
				isError = true;
				$("#directorsError").append("<font color='red'>Directors cannot be empty</font>");
			}
			if ($("#producersId").val() == "") {
				isError = true;
				$("#producersError").append("<font color='red'>Producers cannot be empty</font>");
			}
			if ($("#musicDirectorsId").val() == "") {
				isError = true;
				$("#musicDirectorsError").append("<font color='red'>Music Directors cannot be empty</font>");
			}
			if ($("#actorsId").val() == "") {
				isError = true;
				$("#actorsError").append("<font color='red'>Actors cannot be empty</font>");
			}
			if ($("#actressesId").val() == "") {
				isError = true;
				$("#actressesError").append("<font color='red'>Actresses cannot be empty</font>");
			}
			if ($("#singersId").val() == "") {
				isError = true;
				$("#singersError").append("<font color='red'>Singers cannot be empty</font>");
			}
			if ($("#choreographerId").val() == "") {
				isError = true;
				$("#choreographerError").append("<font color='red'>Choreographer cannot be empty</font>");
			}
			if ($("#supportingStarCastId").val() == "") {
				isError = true;
				$("#supportingStarCastError").append("<font color='red'>Supporting Star Cast cannot be empty</font>");
			}
			if ($("#lyricistId").val() == "") {
				isError = true;
				$("#lyricistError").append("<font color='red'>Lyricist cannot be empty</font>");
			}
			if ($("#releaseDateId").val() == "") {
				isError = true;
				$("#rDateError").append("<font color='red'>Release Date cannot be empty</font>");
			}
			if ($("#productionCompaniesId").val() == "") {
				isError = true;
				$("#productionCompaniesError").append("<font color='red'>Production Companies cannot be empty</font>");
			}
		}
		if ($("#fileSizeId").val() != "") {
			if (!$.isNumeric($("#fileSizeId").val())) {
				isError = true;
				$("#fileSizeError").append("<font color='red'>File Size should be a number</font>");
			}
		}
		if ($("#fileSize240Id").val() != "") {
			if (!$.isNumeric($("#fileSize240Id").val())) {
				isError = true;
				$("#fileSize240Error").append("<font color='red'>File Size 240 should be a number</font>");
			}
		} else if ($("#smartUrl2Id").val() != "") {
			isError = true;
			$("#fileSize240Error").append("<font color='red'>File Size 240 cannot be blank</font>");
		}
		if ($("#fileSize360Id").val() != "") {
			if (!$.isNumeric($("#fileSize360Id").val())) {
				isError = true;
				$("#fileSize360Error").append("<font color='red'>File Size 360 should be a number</font>");
			}
		} else if ($("#smartUrl2Id").val() != "") {
			isError = true;
			$("#fileSize360Error").append("<font color='red'>File Size 360 cannot be blank</font>");
		}
		if ($("#fileSize480Id").val() != "") {
			if (!$.isNumeric($("#fileSize480Id").val())) {
				isError = true;
				$("#fileSize480Error").append("<font color='red'>File Size 480 should be a number</font>");
			}
		} else if ($("#smartUrl2Id").val() != "") {
			isError = true;
			$("#fileSize480Error").append("<font color='red'>File Size 480 cannot be blank</font>");
		}
		if ($("#smartUrl2Id").val() != "") {
			if ($("#smartUrl2Size240Id").val() == "") {
				isError = true;
				$("#smartUrl2Size240Error").append("<font color='red'>Smart Url2 Size240 cannot be blank</font>");
			}
			if ($("#smartUrl2Size360Id").val() == "") {
				isError = true;
				$("#smartUrl2Size360Error").append("<font color='red'>Smart Url2 Size 360 cannot be blank</font>");
			}
			if ($("#smartUrl2Size480Id").val() == "") {
				isError = true;
				$("#smartUrl2Size480Error").append("<font color='red'>Smart Url2 Size 480 cannot be blank</font>");
			}
			if ($("#smartUrl2Size720Id").val() == "") {
				isError = true;
				$("#smartUrl2Size720Error").append("<font color='red'>Smart Url2 Size 720 cannot be blank</font>");
			}
		}
		if (isError) {
			return false;
		} else {
			$("#editContentDialog").dialog("close");
			var category = {
				category_id : $("#categoryIdText").val(),
				category_name : $("#categoryNameId").val()
			};
			var tp = {
				tpId : $("#tpSelectId").val(),
				tpName : $("#tpSelectId :selected").text()
			};
			var genre = {
				genreId : $("#genreSelectId").val(),
				genreName : $("#genreSelectId :selected").text()
			};
			var subGenres = "";
			$("#subGenresSelect :selected").each(function(i, selected) {
				if (i == 0) {
					subGenres = $(selected).text();
				} else {
					subGenres += "#" + $(selected).text();
				}
			});
			var contents = [];

			if (categoryId == 1) {
				var content = {
					contentId : $("#contentIdText").val(),
					displayName : $("#displayNameId").val(),
					category : category,
					tp : tp,
					genre : genre,
					subGenre : subGenres,
					language : $("#languageSelectId").val(),
					search : $("#searchTextId").val(),
					shortDescription : $("#shortDescId").val(),
					longDescription : $("#longDescId").val(),
					rating : $("#ratingId").val(),
					smartUrl1 : $("#smartUrl1Id").val(),
					smartUrl2 : $("#smartUrl2Id").val(),
					smartUrl3 : $("#smartUrl3Id").val(),
					director : $("#directorsId").val(),
					producers : $("#producersId").val(),
					musicDirectors : $("#musicDirectorsId").val(),
					actors : $("#actorsId").val(),
					actresses : $("#actressesId").val(),
					singers : $("#singersId").val(),
					choreographer : $("#choreographerId").val(),
					supportingStarCast : $("#supportingStarCastId").val(),
					lyricist : $("#lyricistId").val(),
					review : $("#reviewId").val(),
					rDate : $("#releaseDateId").val(),
					productionCompanies : $("#productionCompaniesId").val(),
					fileSize : $("#fileSizeId").val(),
					fileSize240 : $("#fileSize240Id").val(),
					fileSize360 : $("#fileSize360Id").val(),
					fileSize480 : $("#fileSize480Id").val(),
					smartUrl2Size240 : $("#smartUrl2Size240Id").val(),
					smartUrl2Size360 : $("#smartUrl2Size360Id").val(),
					smartUrl2Size480 : $("#smartUrl2Size480Id").val(),
					smartUrl2Size720 : $("#smartUrl2Size720Id").val()
				};
				contents[0] = content;
			} else {
				var content = {
					contentId : $("#contentIdText").val(),
					displayName : $("#displayNameId").val(),
					category : category,
					tp : tp,
					genre : genre,
					subGenre : subGenres,
					language : $("#languageSelectId").val(),
					search : $("#searchTextId").val(),
					shortDescription : $("#shortDescId").val(),
					longDescription : $("#longDescId").val(),
					rating : $("#ratingId").val(),
					smartUrl1 : $("#smartUrl1Id").val(),
					smartUrl2 : $("#smartUrl2Id").val(),
					smartUrl3 : $("#smartUrl3Id").val(),
					fileSize : $("#fileSizeId").val(),
					fileSize240 : $("#fileSize240Id").val(),
					fileSize360 : $("#fileSize360Id").val(),
					fileSize480 : $("#fileSize480Id").val(),
					smartUrl2Size240 : $("#smartUrl2Size240Id").val(),
					smartUrl2Size360 : $("#smartUrl2Size360Id").val(),
					smartUrl2Size480 : $("#smartUrl2Size480Id").val(),
					smartUrl2Size720 : $("#smartUrl2Size720Id").val(),
				};
				contents[0] = content;
			}
			$.ajax({
				type : "POST",
				url : "contentUpdation",
				data : {
					contents : contents
				},
				success : function(contents) {

					alert("Content Updated");
				},
			})
		}
	});
});
function getContentEditable(i) {
	$(".modal").show();
	var contentId = $("#contentId" + i).val();
	$("#dialogNumber").val(i);
	$("#editContentDialog").dialog({
		autoOpen : false,
		height : 550,
		width : 1100,
		open : function() {
			$("#subGenresSelect").multipleSelect({
				multiple : true,
				multipleWidth : 190
			});
			$.ajax({
				type : "POST",
				url : "getContentForEdit",
				data : {
					contentId : contentId
				},
				success : function(content) {

					$("#displayNameId").val(content.displayName);
					$("#contentIdText").val(content.contentId);
					$("#categoryNameId").val(content.category.category_name);
					$("#categoryIdText").val(content.category.category_id);
					$("#categoryName").empty();
					$("#categoryName").append(content.category.category_name);
					$("#tpSelectId").val(content.tp.tpId);
					$("#genreSelectId").val(content.genre.genreId);
					if (content.subGenre != null) {
						var subGenres = [];
						subGenres = content.subGenre.split("#");
						$("#subGenresSelect").multipleSelect("setSelects", subGenres);
					}
					$("#languageSelectId").val(content.language);
					$("#searchTextId").val(content.search);
					$("#shortDescId").val(content.shortDescription);
					$("#longDescId").val(content.longDescription);
					$("#ratingId").val(content.rating)
					$("#smartUrl1Id").val(content.smartUrl1);
					$("#smartUrl2Id").val(content.smartUrl2);
					$("#smartUrl3Id").val(content.smartUrl3);
					if (content.category.category_id == 1) {
						$("#directorsId").val(content.directors);
						$("#producersId").val(content.producers);
						$("#musicDirectorsId").val(content.musicDirectors);
						$("#actorsId").val(content.actors);
						$("#actressesId").val(content.actresses);
						$("#singersId").val(content.singers);
						$("#choreographerId").val(content.choreographer);
						$("#supportingStarCastId").val(content.supportingStarCast);
						$("#lyricistId").val(content.lyricist);
						$("#reviewId").val(content.review);
						$("#releaseDateId").val(content.rDate);
						$("#productionCompaniesId").val(content.productionCompanies);
					}
					$("#fileSizeId").val(content.fileSize);
					$("#fileSize240Id").val(content.fileSize240);
					$("#fileSize360Id").val(content.fileSize360);
					$("#fileSize480Id").val(content.fileSize480);
					$("#smartUrl2Size240Id").val(content.smartUrl2Size240);
					$("#smartUrl2Size360Id").val(content.smartUrl2Size360);
					$("#smartUrl2Size480Id").val(content.smartUrl2Size480);
					$("#smartUrl2Size720Id").val(content.smartUrl2Size720);
					$(".modal").hide();
				}
			})
		},
		close : function() {
			$("#editContentDialog").dialog("close");
		}
	}).dialog("open");

}