package br.com.enginer.domain.ui.usercase.helper;

import java.util.ArrayList;
import java.util.List;

import br.com.enginer.domain.ui.usercase.schema.field.behavior.Option;

/**
 * 
 */
public class ComboHelper {
	
	public List<Object> status() {
		List<Object> options = new ArrayList<>();
		options.add(new Option("Andamento", 1));
		options.add(new Option("Processando", 2));
		options.add(new Option("Finalizado", 3));
		options.add(new Option("Aguardando", 4));
		options.add(new Option("Enviado", 5));
		return options;
	}
	
	public List<Object> values() {
		List<Object> options = new ArrayList<>();
		options.add(new Option("Value_1", 1));
		options.add(new Option("Value_2", 2));
		options.add(new Option("Value_3", 3));
		options.add(new Option("Value_4", 4));
		options.add(new Option("Value_5", 5));
		return options;
	}

	public List<Option> consoles() {
		List<Option> options = new ArrayList<>();
		options.add(new Option("PlayStation 5", "ps5"));
		options.add(new Option("PlayStation 4", "ps4"));
		options.add(new Option("Xbox Series X", "xbox_series_x"));
		options.add(new Option("Xbox Series S", "xbox_series_s"));
		options.add(new Option("Xbox One", "xbox_one"));
		options.add(new Option("Nintendo Switch", "nintendo_switch"));
		options.add(new Option("Nintendo Switch Lite", "nintendo_switch_lite"));
		options.add(new Option("Steam Deck", "steam_deck"));
		options.add(new Option("Nintendo 3DS", "nintendo_3ds"));
		options.add(new Option("PlayStation Vita", "ps_vita"));
		options.add(new Option("PlayStation 3", "ps3"));
		options.add(new Option("Xbox 360", "xbox_360"));
		options.add(new Option("Wii U", "wii_u"));
		options.add(new Option("Wii", "wii"));
		options.add(new Option("GameCube", "gamecube"));
		return options;
	}

	public List<Option> clouds() {
		List<Option> options = new ArrayList<>();
		options.add(new Option("Amazon Web Services (AWS)", "aws"));
		options.add(new Option("Microsoft Azure", "azure"));
		options.add(new Option("Google Cloud Platform (GCP)", "gcp"));
		options.add(new Option("IBM Cloud", "ibm_cloud"));
		options.add(new Option("Oracle Cloud", "oracle_cloud"));
		options.add(new Option("Alibaba Cloud", "alibaba_cloud"));
		options.add(new Option("DigitalOcean", "digitalocean"));
		options.add(new Option("Heroku", "heroku"));
		options.add(new Option("Linode", "linode"));
		options.add(new Option("Vultr", "vultr"));
		options.add(new Option("Cloudflare", "cloudflare"));
		options.add(new Option("Netlify", "netlify"));
		options.add(new Option("Vercel", "vercel"));
		return options;
	}

	public List<Option> osDesktops() {
		List<Option> options = new ArrayList<>();
		options.add(new Option("Windows", "windows"));
		options.add(new Option("macOS", "macos"));
		options.add(new Option("Linux", "linux"));
		options.add(new Option("Ubuntu", "ubuntu"));
		options.add(new Option("Debian", "debian"));
		options.add(new Option("Fedora", "fedora"));
		options.add(new Option("Arch Linux", "arch"));
		options.add(new Option("Red Hat", "redhat"));
		options.add(new Option("CentOS", "centos"));
		options.add(new Option("Chrome OS", "chromeos"));
		return options;
	}

	public List<Option> osMobiles() {
		List<Option> options = new ArrayList<>();
		options.add(new Option("Android", "android"));
		options.add(new Option("iOS", "ios"));
		options.add(new Option("HarmonyOS", "harmonyos"));
		options.add(new Option("KaiOS", "kaios"));
		options.add(new Option("Windows Phone", "windows_phone"));
		options.add(new Option("BlackBerry OS", "blackberry_os"));
		options.add(new Option("Symbian", "symbian"));
		return options;
	}

	public List<Option> mobiles() {
		List<Option> options = new ArrayList<>();
		options.add(new Option("Apple", "apple"));
		options.add(new Option("Samsung", "samsung"));
		options.add(new Option("Xiaomi", "xiaomi"));
		options.add(new Option("Motorola", "motorola"));
		options.add(new Option("Realme", "realme"));
		options.add(new Option("OnePlus", "oneplus"));
		options.add(new Option("Huawei", "huawei"));
		options.add(new Option("Asus", "asus"));
		options.add(new Option("Nokia", "nokia"));
		options.add(new Option("Sony", "sony"));
		options.add(new Option("LG", "lg"));
		options.add(new Option("Infinix", "infinix"));
		options.add(new Option("Lenovo", "lenovo"));
		options.add(new Option("Honor", "honor"));
		options.add(new Option("Vivo", "vivo"));
		return options;
	}

	public List<Option> drinks() {
		List<Option> options = new ArrayList<>();
		options.add(new Option("Café", "cafe"));
		options.add(new Option("Chá", "cha"));
		options.add(new Option("Suco de Laranja", "suco_laranja"));
		options.add(new Option("Água com Gás", "agua_com_gas"));
		options.add(new Option("Água Sem Gás", "agua_sem_gas"));
		options.add(new Option("Refrigerante", "refrigerante"));
		options.add(new Option("Cerveja", "cerveja"));
		options.add(new Option("Vinho Tinto", "vinho_tinto"));
		options.add(new Option("Vinho Branco", "vinho_branco"));
		options.add(new Option("Energético", "energetico"));
		options.add(new Option("Smoothie", "smoothie"));
		options.add(new Option("Milkshake", "milkshake"));
		return options;
	}

	public List<Option> colors() {
		List<Option> options = new ArrayList<>();
		options.add(new Option("Vermelho", "Vermelho"));
		options.add(new Option("Azul", "Azul"));
		options.add(new Option("Verde", "Verde"));
		options.add(new Option("Amarelo", "Amarelo"));
		options.add(new Option("Roxo", "Roxo"));
		options.add(new Option("Laranja", "Laranja"));
		options.add(new Option("Rosa", "Rosa"));
		options.add(new Option("Preto", "Preto"));
		options.add(new Option("Branco", "Branco"));
		options.add(new Option("Cinza", "Cinza"));
		options.add(new Option("Marrom", "Marrom"));
		options.add(new Option("Bege", "Bege"));
		options.add(new Option("Turquesa", "Turquesa"));
		options.add(new Option("Dourado", "Dourado"));
		options.add(new Option("Prata", "Prata"));
		return options;
	}

	public List<Option> numbers() {
		List<Option> options = new ArrayList<>();
		options.add(new Option("1", 1));
		options.add(new Option("2", 2));
		options.add(new Option("3", 3));
		options.add(new Option("4", 4));
		options.add(new Option("5", 5));
		options.add(new Option("6", 6));
		options.add(new Option("7", 7));
		options.add(new Option("8", 8));
		options.add(new Option("9", 9));
		options.add(new Option("0", 0));
		return options;
	}
	
	public List<Option> indicadores() {
		List<Option> options = new ArrayList<>();
		options.add(new Option("Indicador 11", 11));
		options.add(new Option("Indicador 22", 22));
		options.add(new Option("Indicador 33", 33));
		options.add(new Option("Indicador 44", 44));
		return options;
	}

	public List<Option> question() {
		List<Option> options = new ArrayList<>();
		options.add(new Option("Yes", "yes"));
		options.add(new Option("No", "No"));
		options.add(new Option("Perhaps", "perhaps"));
		return options;
	}

	public List<Option> confirm() {
		List<Option> options = new ArrayList<>();
		options.add(new Option("Yes", "yes"));
		options.add(new Option("No", "No"));
		return options;
	}

}
