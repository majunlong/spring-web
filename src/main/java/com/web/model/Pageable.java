package com.web.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlTransient;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = false)
@EqualsAndHashCode(callSuper = false, exclude = { "pageNumber", "pageSize", "rows", "pages" })
@XmlAccessorType(XmlAccessType.FIELD)
public abstract class Pageable {

	@XmlTransient
	private int pageNumber = 0;

	@XmlTransient
	private int pageSize = 3;

	@XmlTransient
	private int rows;

	@XmlTransient
	private int pages;

	public void setRows(int rows) {
		this.rows = rows;
		int i = rows % this.pageSize;
		this.pages = i > 0 ? rows / this.pageSize + 1 : rows / this.pageSize;
		if (this.pageNumber < 1 || this.pageNumber > this.pages) {
			this.pageNumber = this.pages;
		}
	}

}
