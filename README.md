# Blackjack in Java on mobile
Pokus o vyhotovení aplikace s využitím herních mechanik karetní hry Blackjack. 
**TODO:**
### Navigace

- [x] Dodělat Bottom navigaci
- [x] Dodělat toolbar

### Bank
- [ ] Vyhotovit ukládání gamblerStats v souboru
- [ ] Upravit xml na změnu savebets
- [ ] Obecně zprovoznit savebets
- [ ] Shared preferences pro savebet
- [ ] odkázat na banksettings activity

### BankActivity Settings
- [ ] savebet založit
- [ ] zprovoznit toolbar
- [ ] implementovat implement
- [ ] úvod v oncreate opravit?

### Blackjack
- [ ] Opravit gameloopu v Blackjacku
- [ ] Převést herní smyčku na metody a onlistenery
- [x] zprovoznit savebets ve hře u doublu
- [ ] Při vzdávání se vyhodit ujišťovací obrazovku a ne jen toast
- [ ] runOnUIThread??? Dej tomu smysl, pls

### Aplikace jako taková
- [ ] Nastavení ikonky
- [x] Nastavení jména aplikace

### Aktivity
- [ ] Vyhotovit jiné aktivity
- [ ] Dát aspoň něco do těch aktivit

### MimoAplikační zbytečnosti
- [x] Aktualizovat README

## VMA seznam požadavků

- [x] Aplikace bude mít alespoň 3 aktivity
- [x] Aplikace bude nabízet (minimálně) 2 navigační prvky: hlavní navigace (bottom - pro výběr aktivity), action bar (toolbar - s názvem aktivity, přechodem na nadřazenou aktivitu, volbami)
- [ ] V alespoň 1 aktivitě bude využit implicitní intent pro provedení akce v jiné aplikaci (např. zobrazení webové stránky, zobrazení místa na mapě, zahájení telefonního hovoru, sdílení dat /prostřednictvím emailu, komunikační aplikace, sociálních sítí, apod./)
- [ ] Bude zajištěno kompletní uložení stavu instance každé aktivity
- [x] Aplikace bude využívat (pro uložení dat ve tvaru key-value) shared preferences
- [ ] Pro ukládání většího množství dat bude použit soubor (soubory) v interní paměti zařízení
- [ ] Pro uložení složitějších strukturovaných dat bude aplikace využívat lokální databázi SQLite
- [ ] V aplikaci bude využit online zdroj dat (předpokládáme připojení zařízení k Internetu), ideálně poskytující data ve formátu JSON, například veřejné nebo vlastní API (přístup k vlastní online databázi)
- [ ] Uživatelské rozhraní aplikace bude přehledné a uživatelsky přívětivé
- [ ] Design aplikace bude (alespoň barevně) přizpůsoben