package com.funongo.cms.bo;

public class Genre {
	private int genreId;
	private String genreName;
	private int genreOrder;
	private String imageUrl;
	private CategoryBO category;

	public Genre() {
	}

	public Genre(int genreId, String genreName) {
		this.genreId = genreId;
		this.genreName = genreName;
	}

	public Genre(int genreId) {
		this.genreId = genreId;
	}

	public int getGenreId() {
		return genreId;
	}

	public void setGenreId(int genreId) {
		this.genreId = genreId;
	}

	public String getGenreName() {
		return genreName;
	}

	public void setGenreName(String genreName) {
		this.genreName = genreName;
	}

	public int getGenreOrder() {
		return genreOrder;
	}

	public void setGenreOrder(int genreOrder) {
		this.genreOrder = genreOrder;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public final CategoryBO getCategory() {
		return category;
	}

	public final void setCategory(CategoryBO category) {
		this.category = category;
	}

}
