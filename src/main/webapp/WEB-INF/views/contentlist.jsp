<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%><%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<c:url value='resources/css/theme.ice.css'/>" />
<link rel="stylesheet" href="<c:url value='resources/css/multipleSelect.css'/>" />
<script src="<c:url value="resources/js/jquery.js"/>"></script>
<script src="<c:url value="resources/js/jquery-ui.js"/>"></script>
<script src="resources/js/jquery.tablesorter.js"></script>
<script src="resources/js/jquery.stickytableheaders.js"></script>
<script src="<c:url value="resources/js/multipleSelect.js"/>"></script>
<script src="resources/js/contentlist.js"></script>
<style type="text/css">
label {
	float: none;
}

.error {
	border: 1em solid red;
}

.tderror {
	background-color: red;
}

.tooltipClass {
	color: red;
}

.scrollable-area {
	width: 100%;
	height: 400px;
	overflow: auto;
	/* -webkit-box-shadow: 2px 2px 10px rgba(50, 50, 50, 0.9);
	-moz-box-shadow: 2px 2px 10px rgba(50, 50, 50, 0.9);
	box-shadow: 2px 2px 10px rgba(50, 50, 50, 0.9); */
}

td, th {
	padding: 5px;
}
</style>
<div align="center" id="loadingDiv">
	<table style="width: 100%;">
		<tr>
			<td colspan="5" align="left" style="background-color: grey; height: 20px;"><font style="font-weight: bold; color: white;">Contents:</font></td>
		</tr>
	</table>
	<div class="scrollable-area">
		<table style="width: 100%;" id="contentFeaturingTable">
			<thead>
				<tr style="border: 1px solid black;">
					<th>Genre Name</th>
					<th>Content Name</th>
					<th>Language</th>
					<th>Rating</th>
					<th>Short Description</th>
					<th>Long Description</th>
					<th>Search</th>
					<th>Uploaded Date</th>
					<th>Uploaded By</th>
					<th>Edit</th>
					<th>Delete</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${wrapper.contents }" var="content" varStatus="count">
					<tr>
						<td align="center"><c:out value="${content.genre.genreName }"></c:out></td>
						<td align="center"><c:out value="${content.displayName}"></c:out></td>
						<td align="center"><c:out value="${content.language}"></c:out></td>
						<td align="center"><c:out value="${content.rating }"></c:out></td>
						<td align="center"><c:out value="${content.shortDescription }"></c:out></td>
						<td align="center"><c:out value="${content.longDescription }"></c:out></td>
						<td align="center"><c:out value="${content.search }"></c:out></td>
						<td align="center"><c:out value="${content.uploadedDate }"></c:out></td>
						<td align="center"><c:out value="${content.uploadedBy}"></c:out></td>
						<td align="center"><a href="#" id="EditContent${count.index }" onclick="getContentEditable(${count.index})">Edit</a></td>
						<td align="center">Delete</td>
					</tr>
					<input type="hidden" id="contentId${count.index }" value="${content.contentId }">
				</c:forEach>
				<c:if test="${counter == 0 }">
					<tr>
						<td colspan="14" align="center"><font size="5px">No Contents </font></td>
					</tr>
				</c:if>
			</tbody>
		</table>
	</div>
</div>
<div class="modal" style="display: none">
	<div class="center">
		<img alt="" src="resources/images/loading_spinner.gif" />
	</div>
</div>
<div id="editContentDialog" title="Edit Content">
	<input type="hidden" id="dialogNumber" name="dialogCount" value="">
	<h3>Content</h3>
	<form class="form-group" action="contentUpdation" id="contentUpdationForm">
		<table width="100%">
			<tr>
				<td width="25%"><label for="displayName" class="control-label">Display Name</label></td>
				<td width="25%"><spring:bind path="content.displayName">
						<input type="text" class="form-control" id="displayNameId" name="displayName" value="${status.value }" maxlength="50">
					</spring:bind> <spring:bind path="content.contentId">
						<input type="hidden" id="contentIdText" name="contentId" value="${content.contentId}">
					</spring:bind></td>
				<td width="25%"><label for="Category" class="control-label">Category</label></td>
				<td width="25%"><label for="Category" class="col-xs-2" id="categoryName"><spring:bind path="content.category.category_name">
							<input type="hidden" id="categoryNameId" name="category.category_name" value="${content.category.category_name }">
						</spring:bind> <spring:bind path="content.category.category_id">
							<input type="hidden" id="categoryIdText" name="category.category_id" value="${content.category.category_id}">
						</spring:bind></label></td>
			</tr>
			<tr>
				<td><label for="displayName" class="control-label col-xs-2"></label></td>
				<td><div class="col-xs-3" id="displayNameError"></div></td>
				<td></td>
				<td></td>
			</tr>
			<tr>
				<td><label for="tp" class="control-label">Content Provider (TP)</label></td>
				<td><spring:bind path="content.tp">
						<select class="form-control" name="tp" id="tpSelectId">
							<c:forEach items="${tps }" var="tp">
								<option value="${tp.tpId }"><c:out value="${tp.tpName }" /></option>
							</c:forEach>
						</select>
					</spring:bind></td>
				<td><label for="Genre" class="control-label">Genre</label></td>
				<td><spring:bind path="content.genre">
						<select class="form-control" name="genre" id="genreSelectId">
							<c:forEach items="${genres }" var="item">
								<option value="${item.genreId }" <c:if test="${content.genre.genreId == item.genreId }"> selected="selected"</c:if>><c:out value="${item.genreName }" /></option>
							</c:forEach>
						</select>
					</spring:bind></td>
			</tr>
			<tr>
				<td><label for="Genre" class="control-label">Sub Genres</label></td>
				<td><spring:bind path="content.subGenre">
						<select name="subGenre" id="subGenresSelect" multiple="multiple">
							<c:forEach items="${genres }" var="item">
								<option value="${item.genreName }" <c:if test="${content.genre.genreName == item.genreName }"> selected="selected"</c:if>><c:out value="${item.genreName }" /></option>
							</c:forEach>
						</select>
					</spring:bind></td>
				<td><label for="tp" class="control-label">Language</label></td>
				<td><spring:bind path="content.language">
						<select class="form-control" name="language" id="languageSelectId">
							<c:forEach items="${languages }" var="language">
								<option value="${language }" <c:if test="${content.language == language }"> selected="selected"</c:if>><c:out value="${language}" /></option>
							</c:forEach>
						</select>
					</spring:bind></td>
			</tr>
			<tr>
				<td><label for="Search" class="control-label">Search</label></td>
				<td colspan="3"><spring:bind path="content.search">
						<textarea rows="5" cols="5" name="search" id="searchTextId" class="form-control" maxlength="250"><c:out value="${content.search }" /></textarea>
					</spring:bind></td>
			</tr>
			<tr>
				<td><label class="control-label col-xs-2"></label></td>
				<td colspan="3"><div class="col-xs-6" id="searchError"></div></td>
			</tr>
			<tr>
				<td><label for="Short Description" class="control-label">Short Description</label></td>
				<td colspan="3"><spring:bind path="content.shortDescription">
						<textarea rows="5" cols="5" name="shortDescription" id="shortDescId" class="form-control" maxlength="250"><c:out value="${content.shortDescription }" /></textarea>
					</spring:bind></td>
			</tr>
			<tr>
				<td><label class="control-label col-xs-2"></label></td>
				<td colspan="3"><div class="col-xs-6" id="shortDescError"></div></td>
			</tr>
			<tr>
				<td><label for="Long Description" class="control-label">Long Description</label></td>
				<td colspan="3"><spring:bind path="content.longDescription">
						<textarea rows="5" cols="5" name="longDescription" id="longDescId" class="form-control"><c:out value="${content.longDescription }" /></textarea>
					</spring:bind></td>
			</tr>
			<tr>
				<td><label class="control-label"></label></td>
				<td colspan="3"><div class="col-xs-3" id="longDescError"></div></td>
			</tr>
			<tr>
				<td><label for="Rating" class="control-label">Rating</label></td>
				<td><spring:bind path="content.rating">
						<input type="text" class="form-control" id="ratingId" name="rating" value="${status.value }">
					</spring:bind></td>
				<td><label for="smartUrl1" class="control-label">Smart Url1</label></td>
				<td><spring:bind path="content.smartUrl1">
						<input type="text" class="form-control" id="smartUrl1Id" name="smartUrl1" value="${status.value }" maxlength="200">
					</spring:bind></td>
			</tr>
			<tr>
				<td><label class="control-label"></label></td>
				<td><div class="col-xs-3" id="ratingError"></div></td>
				<td><label class="control-label"></label></td>
				<td><div class="col-xs-3" id="smartUrl1Error"></div></td>
			</tr>
			<tr>
				<td><label for="smartUrl2" class="control-label">Smart Url2</label></td>
				<td><spring:bind path="content.smartUrl2">
						<input type="text" class="form-control" id="smartUrl2Id" name="smartUrl2" value="${status.value }" maxlength="200">
					</spring:bind></td>
				<td><label for="smartUrl3" class="control-label">Smart Url3</label></td>
				<td><spring:bind path="content.smartUrl3">
						<input type="text" class="form-control" id="smartUrl3Id" name="smartUrl3" value="${status.value }" maxlength="200">
					</spring:bind></td>
			</tr>
			<tr>
				<td><label class="control-label"></label></td>
				<td><div class="col-xs-3" id="smartUrl2Error"></div></td>
				<td><label class="control-label"></label></td>
				<td><div class="col-xs-3" id="smartUrl3Error"></div></td>
			</tr>
			<c:if test="${content.category.category_id == 1 }">
				<tr>
					<td><label for="Directors" class="control-label">Directors</label></td>
					<td><spring:bind path="content.directors">
							<input type="text" class="form-control" id="directorsId" name="director" value="${status.value }" maxlength="100">
						</spring:bind></td>
					<td><label for="Producers" class="control-label">Producers</label></td>
					<td><spring:bind path="content.producers">
							<input type="text" class="form-control" id="producersId" name="producers" value="${status.value }" maxlength="100">
						</spring:bind></td>
				</tr>
				<tr>
					<td><label class="control-label"></label></td>
					<td><div class="col-xs-3" id="directorsError"></div></td>
					<td><label class="control-label"></label></td>
					<td><div class="col-xs-3" id="producersError"></div></td>
				</tr>
				<tr>
					<td><label for="musicDirectors" class="control-label">Music Directors</label></td>
					<td><spring:bind path="content.musicDirectors">
							<input type="text" class="form-control" id="musicDirectorsId" name="musicDirectors" value="${status.value }" maxlength="100">
						</spring:bind></td>
					<td><label for="actors" class="control-label">Actors</label></td>
					<td><spring:bind path="content.actors">
							<input type="text" class="form-control" id="actorsId" name="actors" value="${status.value }" maxlength="200">
						</spring:bind></td>
				</tr>
				<tr>
					<td><label class="control-label"></label></td>
					<td><div class="col-xs-3" id="musicDirectorsError"></div></td>
					<td><label class="control-label"></label></td>
					<td><div class="col-xs-3" id="actorsError"></div></td>
				</tr>
				<tr>
					<td><label for="actresses" class="control-label">Actresses</label></td>
					<td><spring:bind path="content.actresses">
							<input type="text" class="form-control" id="actressesId" name="actresses" value="${status.value }" maxlength="200">
						</spring:bind></td>
					<td><label for="singers" class="control-label">Singers</label></td>
					<td><spring:bind path="content.singers">
							<input type="text" class="form-control" id="singersId" name="singers" value="${status.value }" maxlength="300">
						</spring:bind></td>
				</tr>
				<tr>
					<td><label class="control-label"></label></td>
					<td><div class="col-xs-3" id="actressesError"></div></td>
					<td><label class="control-label"></label></td>
					<td><div class="col-xs-3" id="singersError"></div></td>
				</tr>
				<tr>
					<td><label for="choreographer" class="control-label">Choreographer</label></td>
					<td><spring:bind path="content.choreographer">
							<input type="text" class="form-control" id="choreographerId" name="choreographer" value="${status.value }" maxlength="200">
						</spring:bind></td>
					<td><label for="supportingStarCast" class="control-label">Supporting Star Cast</label></td>
					<td><spring:bind path="content.supportingStarCast">
							<input type="text" class="form-control" id="supportingStarCastId" name="supportingStarCast" value="${status.value }" maxlength="500">
						</spring:bind></td>
				</tr>
				<tr>
					<td><label class="control-label"></label></td>
					<td><div class="col-xs-3" id="choreographerError"></div></td>
					<td><label class="control-label"></label></td>
					<td><div class="col-xs-3" id="supportingStarCastError"></div></td>
				</tr>
				<tr>
					<td><label for="lyricist" class="control-label">Lyricist</label></td>
					<td><spring:bind path="content.lyricist">
							<input type="text" class="form-control" id="lyricistId" name="lyricist" value="${status.value }" maxlength="100">
						</spring:bind></td>
					<td><label for="review" class="control-label">Review</label></td>
					<td><spring:bind path="content.review">
							<input type="text" class="form-control" id="reviewId" name="review" value="${status.value }" maxlength="300">
						</spring:bind></td>
				</tr>
				<tr>
					<td><label class="control-label"></label></td>
					<td><div class="col-xs-3" id="lyricistError"></div></td>
					<td></td>
					<td></td>
				</tr>
				<tr>
					<td><label for="releaseDate" class="control-label">Release Date</label></td>
					<td><spring:bind path="content.rDate">
							<input type="text" class="form-control" id="releaseDateId" name="rDate" value="${status.value }" readonly="readonly">
						</spring:bind></td>
					<td><label for="productionCompanies" class="control-label">Production Companies</label></td>
					<td><spring:bind path="content.productionCompanies">
							<input type="text" class="form-control" id="productionCompaniesId" name="productionCompanies" value="${status.value }" maxlength="200">
						</spring:bind></td>
				</tr>
				<tr>
					<td><label class="control-label"></label></td>
					<td><div class="col-xs-3" id="rDateError"></div></td>
					<td><label class="control-label"></label></td>
					<td><div class="col-xs-3" id="productionCompaniesError"></div></td>
				</tr>
			</c:if>
			<tr>
				<td><label for="fileSize" class="control-label">File Size</label></td>
				<td><spring:bind path="content.fileSize">
						<input type="text" class="form-control" id="fileSizeId" name="fileSize" value="${status.value }">
					</spring:bind></td>
				<td><label for="fileSize240" class="control-label">File Size240</label></td>
				<td><spring:bind path="content.fileSize240">
						<input type="text" class="form-control" id="fileSize240Id" name="fileSize240" value="${status.value }">
					</spring:bind></td>
			</tr>
			<tr>
				<td><label class="control-label"></label></td>
				<td><div class="col-xs-3" id="fileSizeError"></div></td>
				<td><label class="control-label"></label></td>
				<td><div class="col-xs-3" id="fileSize240Error"></div></td>
			</tr>
			<tr>
				<td><label for="fileSize360" class="control-label">File Size360</label></td>
				<td><spring:bind path="content.fileSize360">
						<input type="text" class="form-control" id="fileSize360Id" name="fileSize360" value="${status.value }">
					</spring:bind></td>
				<td><label for="fileSize480" class="control-label">File Size480</label></td>
				<td><spring:bind path="content.fileSize480">
						<input type="text" class="form-control" id="fileSize480Id" name="fileSize480" value="${status.value }">
					</spring:bind></td>
			</tr>
			<tr>
				<td><label class="control-label"></label></td>
				<td><div class="col-xs-3" id="fileSize360Error"></div></td>
				<td><label class="control-label"></label></td>
				<td><div class="col-xs-3" id="fileSize480Error"></div></td>
			</tr>
			<tr>
				<td><label for="smartUrl2Size240" class="control-label">Smart Url2 Size240</label></td>
				<td><spring:bind path="content.smartUrl2Size240">
						<input type="text" class="form-control" id="smartUrl2Size240Id" name="smartUrl2Size240" value="${status.value }" maxlength="200">
					</spring:bind></td>
				<td><label for="smartUrl2Size360" class="control-label">Smart Url2 Size360</label></td>
				<td><spring:bind path="content.smartUrl2Size360">
						<input type="text" class="form-control" id="smartUrl2Size360Id" name="smartUrl2Size360" value=" ${status.value }" maxlength="200">
					</spring:bind></td>
			</tr>
			<tr>
				<td><label class="control-label"></label></td>
				<td><div class="col-xs-3" id="smartUrl2Size240Error"></div></td>
				<td></td>
				<td><div class="col-xs-3" id="smartUrl2Size360Error"></div></td>
			</tr>
			<tr>
				<td><label for="smartUrl2Size480" class="control-label">Smart Url2 Size480</label></td>
				<td><spring:bind path="content.smartUrl2Size480">
						<input type="text" class="form-control" id="smartUrl2Size480Id" name="smartUrl2Size480" value="${status.value }" maxlength="200">
					</spring:bind></td>
				<td><label for="smartUrl2Size720" class="control-label">Smart Url2 Size720</label></td>
				<td><spring:bind path="content.smartUrl2Size720">
						<input type="text" class="form-control" id="smartUrl2Size720Id" name="smartUrl2Size720" value="${status.value }" maxlength="200">
					</spring:bind></td>
			</tr>
			<tr>
				<td></td>
				<td><div class="col-xs-3" id="smartUrl2Size480Error"></div></td>
				<td></td>
				<td><div class="col-xs-3" id="smartUrl2Size720Error"></div></td>
			</tr>
			<tr>
				<td colspan="4"><div class="col-xs-offset-6 col-xs-10">
						<button type="button" class="btn btn-primary" id="updateContentButton">Update</button>
					</div></td>
			</tr>
		</table>
	</form>
</div>
<input type="hidden" id="listSize" name="contentCount" value="${fn:length(contentPrice.contents) }">
