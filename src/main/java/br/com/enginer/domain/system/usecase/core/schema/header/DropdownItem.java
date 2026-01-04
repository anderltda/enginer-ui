package br.com.enginer.domain.system.usecase.core.schema.header;

/**
 * 
 */
public class DropdownItem {

	private String key;
	private String icon;
	private String label;
	private String badgeType; // "api" | "help" | "files" | "tags" | "timeline" | "calendar"

	public DropdownItem() {
		super();
	}

	public DropdownItem(String key, String icon, String label, String badgeType) {
		super();
		this.key = key;
		this.icon = icon;
		this.label = label;
		this.badgeType = badgeType;
	}

	/**
	 * @return the key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * @param key the key to set
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * @return the icon
	 */
	public String getIcon() {
		return icon;
	}

	/**
	 * @param icon the icon to set
	 */
	public void setIcon(String icon) {
		this.icon = icon;
	}

	/**
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * @param label the label to set
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * @return the badgeType
	 */
	public String getBadgeType() {
		return badgeType;
	}

	/**
	 * @param badgeType the badgeType to set
	 */
	public void setBadgeType(String badgeType) {
		this.badgeType = badgeType;
	}

}
