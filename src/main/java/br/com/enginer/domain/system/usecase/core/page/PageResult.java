package br.com.enginer.domain.system.usecase.core.page;

import java.util.List;

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
