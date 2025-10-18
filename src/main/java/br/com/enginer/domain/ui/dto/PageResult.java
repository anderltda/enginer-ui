package br.com.enginer.domain.ui.dto;

import java.util.List;

import br.com.enginer.domain.ui.dto.page.Page;

/**
 * @param <T>
 */
public class PageResult<T> {

	private Page page;

	private List<T> content;

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}

	public List<T> getContent() {
		return content;
	}

	public void setContent(List<T> content) {
		this.content = content;
	}

}
