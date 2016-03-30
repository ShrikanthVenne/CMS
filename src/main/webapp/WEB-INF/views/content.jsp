<%@ page language="java" contentType="text/html; charset=UTF-16" pageEncoding="UTF-16"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-16">
<link rel="stylesheet" href="resources/css/bootstrap.min.css">
<link rel="stylesheet" href="<c:url value='resources/css/multipleSelect.css'/>" />
<link rel="stylesheet" href="resources/css/main.css">
<link rel="stylesheet" href="<c:url value='resources/css/jquery-ui.css'/>">
<script src="<c:url value="resources/js/jquery.js"/>"></script>
<script src="<c:url value="resources/js/jquery-ui.js"/>"></script>
<script src="<c:url value="resources/js/multipleSelect.js"/>"></script>
<script src="<c:url value="resources/js/content.js"/>"></script>
<title>Edit Content</title>
<script type="text/javascript">
	
</script>
</head>
<body>
	<div class="container">
		<div class="row">
			<div class="form-group">
				<label for="displayName" class="control-label col-xs-2">Display Name</label>
				<div class="col-xs-3">
					<spring:bind path="content.displayName">
						<input type="text" class="form-control" id="displayNameId" name="displayName" value="${status.value }" maxlength="50">
					</spring:bind>
					<spring:bind path="content.contentId">
						<input type="hidden" id="contentIdText" name="contentId" value="${content.contentId}">
					</spring:bind>
				</div>
			</div>
			<div class="form-group">
				<label for="Category" class="control-label col-xs-2">Category</label> <label for="Category" class="col-xs-2" id="categoryName"></label>
				<spring:bind path="content.category.category_name">
					<input type="hidden" id="categoryNameId" name="category.category_name" value="${content.category.category_name }">
				</spring:bind>
				<spring:bind path="content.category.category_id">
					<input type="hidden" id="categoryIdText" name="category.category_id" value="${content.category.category_id}">
				</spring:bind>
			</div>
		</div>
		<div class="row">
			<div class="form-group">
				<label for="displayName" class="control-label col-xs-2"></label>
				<div class="col-xs-3" id="displayNameError"></div>
			</div>
		</div>
		<div class="row">
			<div class="form-group">
				<label for="tp" class="control-label col-xs-2">Content Provider (TP)</label>
				<div class="col-xs-3">
					<spring:bind path="content.tp">
						<select class="form-control" name="tp" id="tpSelectId">
							<c:forEach items="${tps }" var="tp">
								<option value="${tp.tpId }"><c:out value="${tp.tpName }" /></option>
							</c:forEach>
						</select>
					</spring:bind>
				</div>
			</div>
			<div class="form-group">
				<label for="Genre" class="control-label col-xs-2">Genre</label>
				<div class="col-xs-3">
					<spring:bind path="content.genre">
						<select class="form-control" name="genre" id="genreSelectId">
							<c:forEach items="${genres }" var="item">
								<option value="${item.genreId }" <c:if test="${content.genre.genreId == item.genreId }"> selected="selected"</c:if>><c:out value="${item.genreName }" /></option>
							</c:forEach>
						</select>
					</spring:bind>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="form-group">
				<label for="Genre" class="control-label col-xs-2">Sub Genres</label>
				<div class="col-xs-3">
					<spring:bind path="content.subGenre">
						<select name="subGenre" id="subGenresSelect" multiple="multiple">
							<c:forEach items="${genres }" var="item">
								<option value="${item.genreId }" <c:if test="${content.genre.genreId == item.genreId }"> selected="selected"</c:if>><c:out value="${item.genreName }" /></option>
							</c:forEach>
						</select>
					</spring:bind>
				</div>
			</div>
			<div class="form-group">
				<label for="tp" class="control-label col-xs-2">Language</label>
				<div class="col-xs-3">
					<spring:bind path="content.language">
						<select class="form-control" name="language" id="languageSelectId">
							<c:forEach items="${languages }" var="language">
								<option value="${language }" <c:if test="${content.language == language }"> selected="selected"</c:if>><c:out value="${language}" /></option>
							</c:forEach>
						</select>
					</spring:bind>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="form-group">
				<label for="Search" class="control-label col-xs-2">Search</label>
				<div class="col-xs-6">
					<spring:bind path="content.search">
						<textarea rows="5" cols="5" name="search" id="searchTextId" class="form-control" maxlength="250"><c:out value="${content.search }" /></textarea>
					</spring:bind>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="form-group">
				<label class="control-label col-xs-2"></label>
				<div class="col-xs-3" id="searchError"></div>
			</div>
		</div>
		<div class="row">
			<div class="form-group">
				<label for="Short Description" class="control-label col-xs-2">Short Description</label>
				<div class="col-xs-6">
					<spring:bind path="content.shortDescription">
						<textarea rows="5" cols="5" name="shortDescription" id="shortDescId" class="form-control" maxlength="250"><c:out value="${content.shortDescription }" /></textarea>
					</spring:bind>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="form-group">
				<label class="control-label col-xs-2"></label>
				<div class="col-xs-3" id="shortDescError"></div>
			</div>
		</div>
		<div class="row">
			<div class="form-group">
				<label for="Long Description" class="control-label col-xs-2">Long Description</label>
				<div class="col-xs-6">
					<spring:bind path="content.longDescription">
						<textarea rows="5" cols="5" name="longDescription" id="longDescId" class="form-control"><c:out value="${content.longDescription }" /></textarea>
					</spring:bind>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="form-group">
				<label class="control-label col-xs-2"></label>
				<div class="col-xs-3" id="longDescError"></div>
			</div>
		</div>
		<div class="row">
			<div class="form-group">
				<label for="Rating" class="control-label col-xs-2">Rating</label>
				<div class="col-xs-3">
					<spring:bind path="content.rating">
						<input type="text" class="form-control" id="ratingId" name="rating" value="${status.value }">
					</spring:bind>
				</div>
			</div>
			<div class="form-group">
				<label for="smartUrl1" class="control-label col-xs-2">Smart Url1</label>
				<div class="col-xs-3">
					<spring:bind path="content.smartUrl1">
						<input type="text" class="form-control" id="smartUrl1Id" name="smartUrl1" value="${status.value }" maxlength="200">
					</spring:bind>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="form-group">
				<label class="control-label col-xs-2"></label>
				<div class="col-xs-3" id="ratingError"></div>
			</div>
			<div class="form-group">
				<label class="control-label col-xs-2"></label>
				<div class="col-xs-3" id="smartUrl1Error"></div>
			</div>
		</div>
		<div class="row">
			<div class="form-group">
				<label for="smartUrl2" class="control-label col-xs-2">Smart Url2</label>
				<div class="col-xs-3">
					<spring:bind path="content.smartUrl2">
						<input type="text" class="form-control" id="smartUrl2Id" name="smartUrl2" value="${status.value }" maxlength="200">
					</spring:bind>
				</div>
			</div>
			<div class="form-group">
				<label for="smartUrl3" class="control-label col-xs-2">Smart Url3</label>
				<div class="col-xs-3">
					<spring:bind path="content.smartUrl3">
						<input type="text" class="form-control" id="smartUrl3Id" name="smartUrl3" value="${status.value }" maxlength="200">
					</spring:bind>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="form-group">
				<label class="control-label col-xs-2"></label>
				<div class="col-xs-3" id="smartUrl2Error"></div>
			</div>
			<div class="form-group">
				<label class="control-label col-xs-2"></label>
				<div class="col-xs-3" id="smartUrl3Error"></div>
			</div>
		</div>
		<c:if test="${content.category.category_id == 1 }">
			<div class="row">
				<div class="form-group">
					<label for="Directors" class="control-label col-xs-2">Directors</label>
					<div class="col-xs-3">
						<spring:bind path="content.directors">
							<input type="text" class="form-control" id="directorsId" name="director" value="${status.value }" maxlength="100">
						</spring:bind>
					</div>
				</div>
				<div class="form-group">
					<label for="Producers" class="control-label col-xs-2">Producers</label>
					<div class="col-xs-3">
						<spring:bind path="content.producers">
							<input type="text" class="form-control" id="producersId" name="producers" value="${status.value }" maxlength="100">
						</spring:bind>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="form-group">
					<label class="control-label col-xs-2"></label>
					<div class="col-xs-3" id="directorsError"></div>
				</div>
				<div class="form-group">
					<label class="control-label col-xs-2"></label>
					<div class="col-xs-3" id="producersError"></div>
				</div>
			</div>
			<div class="row">
				<div class="form-group">
					<label for="musicDirectors" class="control-label col-xs-2">Music Directors</label>
					<div class="col-xs-3">
						<spring:bind path="content.musicDirectors">
							<input type="text" class="form-control" id="musicDirectorsId" name="musicDirectors" value="${status.value }" maxlength="100">
						</spring:bind>
					</div>
				</div>
				<div class="form-group">
					<label for="actors" class="control-label col-xs-2">Actors</label>
					<div class="col-xs-3">
						<spring:bind path="content.actors">
							<input type="text" class="form-control" id="actorsId" name="actors" value="${status.value }" maxlength="200">
						</spring:bind>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="form-group">
					<label class="control-label col-xs-2"></label>
					<div class="col-xs-3" id="musicDirectorsError"></div>
				</div>
				<div class="form-group">
					<label class="control-label col-xs-2"></label>
					<div class="col-xs-3" id="actorsError"></div>
				</div>
			</div>
			<div class="row">
				<div class="form-group">
					<label for="actresses" class="control-label col-xs-2">Actresses</label>
					<div class="col-xs-3">
						<spring:bind path="content.actresses">
							<input type="text" class="form-control" id="actressesId" name="actresses" value="${status.value }" maxlength="200">
						</spring:bind>
					</div>
				</div>
				<div class="form-group">
					<label for="singers" class="control-label col-xs-2">Singers</label>
					<div class="col-xs-3">
						<spring:bind path="content.singers">
							<input type="text" class="form-control" id="singersId" name="singers" value="${status.value }" maxlength="300">
						</spring:bind>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="form-group">
					<label class="control-label col-xs-2"></label>
					<div class="col-xs-3" id="actressesError"></div>
				</div>
				<div class="form-group">
					<label class="control-label col-xs-2"></label>
					<div class="col-xs-3" id="singersError"></div>
				</div>
			</div>
			<div class="row">
				<div class="form-group">
					<label for="choreographer" class="control-label col-xs-2">Choreographer</label>
					<div class="col-xs-3">
						<spring:bind path="content.choreographer">
							<input type="text" class="form-control" id="choreographerId" name="choreographer" value="${status.value }" maxlength="200">
						</spring:bind>
					</div>
				</div>
				<div class="form-group">
					<label for="supportingStarCast" class="control-label col-xs-2">Supporting Star Cast</label>
					<div class="col-xs-3">
						<spring:bind path="content.supportingStarCast">
							<input type="text" class="form-control" id="supportingStarCastId" name="supportingStarCast" value="${status.value }" maxlength="500">
						</spring:bind>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="form-group">
					<label class="control-label col-xs-2"></label>
					<div class="col-xs-3" id="choreographerError"></div>
				</div>
				<div class="form-group">
					<label class="control-label col-xs-2"></label>
					<div class="col-xs-3" id="supportingStarCastError"></div>
				</div>
			</div>
			<div class="row">
				<div class="form-group">
					<label for="lyricist" class="control-label col-xs-2">Lyricist</label>
					<div class="col-xs-3">
						<spring:bind path="content.lyricist">
							<input type="text" class="form-control" id="lyricistId" name="lyricist" value="${status.value }" maxlength="100">
						</spring:bind>
					</div>
				</div>
				<div class="form-group">
					<label for="review" class="control-label col-xs-2">Review</label>
					<div class="col-xs-3">
						<spring:bind path="content.review">
							<input type="text" class="form-control" id="reviewId" name="review" value="${status.value }" maxlength="300">
						</spring:bind>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="form-group">
					<label class="control-label col-xs-2"></label>
					<div class="col-xs-3" id="lyricistError"></div>
				</div>
			</div>
			<div class="row">
				<div class="form-group">
					<label for="releaseDate" class="control-label col-xs-2">Release Date</label>
					<div class="col-xs-3">
						<spring:bind path="content.rDate">
							<input type="text" class="form-control" id="releaseDateId" name="rDate" value="${status.value }" readonly="readonly">
						</spring:bind>
					</div>
				</div>
				<div class="form-group">
					<label for="productionCompanies" class="control-label col-xs-2">Production Companies</label>
					<div class="col-xs-3">
						<spring:bind path="content.productionCompanies">
							<input type="text" class="form-control" id="productionCompaniesId" name="productionCompanies" value="${status.value }" maxlength="200">
						</spring:bind>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="form-group">
					<label class="control-label col-xs-2"></label>
					<div class="col-xs-3" id="rDateError"></div>
				</div>
				<div class="form-group">
					<label class="control-label col-xs-2"></label>
					<div class="col-xs-3" id="productionCompaniesError"></div>
				</div>
			</div>
		</c:if>
		<div class="row">
			<div class="form-group">
				<label for="fileSize" class="control-label col-xs-2">File Size</label>
				<div class="col-xs-3">
					<spring:bind path="content.fileSize">
						<input type="text" class="form-control" id="fileSizeId" name="fileSize" value="${status.value }">
					</spring:bind>
				</div>
			</div>
			<div class="form-group">
				<label for="fileSize240" class="control-label col-xs-2">File Size240</label>
				<div class="col-xs-3">
					<spring:bind path="content.fileSize240">
						<input type="text" class="form-control" id="fileSize240Id" name="fileSize240" value="${status.value }">
					</spring:bind>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="form-group">
				<label class="control-label col-xs-2"></label>
				<div class="col-xs-3" id="fileSizeError"></div>
			</div>
			<div class="form-group">
				<label class="control-label col-xs-2"></label>
				<div class="col-xs-3" id="fileSize240Error"></div>
			</div>
		</div>
		<div class="row">
			<div class="form-group">
				<label for="fileSize360" class="control-label col-xs-2">File Size360</label>
				<div class="col-xs-3">
					<spring:bind path="content.fileSize360">
						<input type="text" class="form-control" id="fileSize360Id" name="fileSize360" value="${status.value }">
					</spring:bind>
				</div>
			</div>
			<div class="form-group">
				<label for="fileSize480" class="control-label col-xs-2">File Size480</label>
				<div class="col-xs-3">
					<spring:bind path="content.fileSize480">
						<input type="text" class="form-control" id="fileSize480Id" name="fileSize480" value="${status.value }">
					</spring:bind>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="form-group">
				<label class="control-label col-xs-2"></label>
				<div class="col-xs-3" id="fileSize360Error"></div>
			</div>
			<div class="form-group">
				<label class="control-label col-xs-2"></label>
				<div class="col-xs-3" id="fileSize480Error"></div>
			</div>
		</div>
		<div class="row">
			<div class="form-group">
				<label for="smartUrl2Size240" class="control-label col-xs-2">Smart Url2 Size240</label>
				<div class="col-xs-3">
					<spring:bind path="content.smartUrl2Size240">
						<input type="text" class="form-control" id="smartUrl2Size240Id" name="smartUrl2Size240" value="${status.value }" maxlength="200">
					</spring:bind>
				</div>
			</div>
			<div class="form-group">
				<label for="smartUrl2Size360" class="control-label col-xs-2">Smart Url2 Size360</label>
				<div class="col-xs-3">
					<spring:bind path="content.smartUrl2Size360">
						<input type="text" class="form-control" id="smartUrl2Size360Id" name="smartUrl2Size360" value=" ${status.value }" maxlength="200">
					</spring:bind>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="form-group">
				<label class="control-label col-xs-2"></label>
				<div class="col-xs-3" id="smartUrl2Size240Error"></div>
			</div>
			<div class="form-group">
				<label class="control-label col-xs-2"></label>
				<div class="col-xs-3" id="smartUrl2Size360Error"></div>
			</div>
		</div>
		<div class="row">
			<div class="form-group">
				<label for="smartUrl2Size480" class="control-label col-xs-2">Smart Url2 Size480</label>
				<div class="col-xs-3">
					<spring:bind path="content.smartUrl2Size480">
						<input type="text" class="form-control" id="smartUrl2Size480Id" name="smartUrl2Size480" value="${status.value }" maxlength="200">
					</spring:bind>
				</div>
			</div>
			<div class="form-group">
				<label for="smartUrl2Size720" class="control-label col-xs-2">Smart Url2 Size720</label>
				<div class="col-xs-3">
					<spring:bind path="content.smartUrl2Size720">
						<input type="text" class="form-control" id="smartUrl2Size720Id" name="smartUrl2Size720" value="${status.value }" maxlength="200">
					</spring:bind>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="form-group">
				<label class="control-label col-xs-2"></label>
				<div class="col-xs-3" id="smartUrl2Size480Error"></div>
			</div>
			<div class="form-group">
				<label class="control-label col-xs-2"></label>
				<div class="col-xs-3" id="smartUrl2Size720Error"></div>
			</div>
		</div>
		<div class="form-group">
			<div class="col-xs-offset-2 col-xs-10">
				<button type="button" class="btn btn-primary" id="updateContentButton">Update</button>
			</div>
		</div>
	</form>
</div>
	</div>
</body>
</html>