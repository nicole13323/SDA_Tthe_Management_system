import i18n from 'i18next';
import { initReactI18next } from 'react-i18next';
import LanguageDetector from 'i18next-browser-languagedetector';

const rw = {
  appName: 'Icyacumi SDA', logout: 'Sohoka',
  loginTitle: 'Injira', loginSubtitle: 'Gucunga icyacumi mu matorero ya SDA mu Rwanda',
  identifier: "Telefoni, imeri, izina/ID y'itorero, cyangwa izina ry'ukoresha", password: 'Ijambobanga',
  signIn: 'Injira', signingIn: 'Kwinjira…',
  loginHelp: "Abanyetorero: telefoni cyangwa imeri. Amatorero: izina cyangwa ID. Umucungamutungo w'icyacumi: izina ry'ukoresha.",
  loading: 'Birimo gutegurwa…', empty: 'Nta makuru ahari',
  amount: 'Amafaranga', method: 'Uburyo bwo kwishyura', date: 'Itariki', receipt: 'Inyemezabwishyu',
  reference: 'Nimero y’ubwishyu', member: 'Umunyetorero', church: 'Itorero', total: 'Igiteranyo',
  payments: 'Ubwishyu', phone: 'Telefoni', email: 'Imeri', fullName: 'Amazina yose', language: 'Ururimi',
  payTitle: 'Tanga icyacumi', payerPhone: 'Telefoni yo kwishyuriraho', payNow: 'Ishyura ubu', paying: 'Kwishyura…',
  paySuccess: 'Ubwishyu bwakiriwe. Murakoze!', history: 'Amateka y’ubwishyu bwanjye',
  noHistory: 'Nta cyacumi urishyura', receiptTitle: 'Inyemezabwishyu y’icyacumi', print: 'Priyinta', close: 'Funga',
  hello: 'Muraho, {{name}}',
  members: 'Abanyetorero', addMember: 'Ongeramo umunyetorero', addMemberTitle: 'Umunyetorero mushya', save: 'Bika',
  memberAdded: 'Umunyetorero yongewemo. Ijambobanga ry’ibanze ni 1234.', noMembers: 'Nta munyetorero uraboneka',
  reportTitle: 'Raporo y’icyacumi', week: 'Icyumweru', month: 'Ukwezi', year: 'Umwaka',
  previous: 'Ibyabanje', next: 'Ibikurikira', totalCollected: 'Icyakusanyijwe', paymentCount: 'Umubare w’ubwishyu',
  cashTitle: 'Andika icyacumi cy’amafaranga (cash)', searchMember: 'Shaka umunyetorero',
  searchPlaceholder: 'Izina cyangwa telefoni', selectMember: 'Hitamo', recordCash: 'Andika ubwishyu bwa cash',
  cashRecorded: 'Ubwishyu bwa cash bwanditswe', allChurches: 'Amatorero yose', cashBadge: 'Cash',
  methods: { MTN_MOMO: 'MTN Mobile Money', AIRTEL_MONEY: 'Airtel Money', CARD: 'Ikarita ya banki', BANK: 'Kohereza kuri banki', CASH: 'Amafaranga (cash)' },
  error: 'Hari ikitagenze neza. Ongera ugerageze.'
};

const sw = {
  appName: 'Zaka SDA', logout: 'Toka',
  loginTitle: 'Ingia', loginSubtitle: 'Usimamizi wa zaka kwa makanisa ya SDA nchini Rwanda',
  identifier: 'Simu, barua pepe, jina/ID ya kanisa, au jina la mtumiaji', password: 'Nenosiri',
  signIn: 'Ingia', signingIn: 'Inaingia…',
  loginHelp: 'Washiriki: simu au barua pepe. Makanisa: jina au ID. Meneja wa Zaka: jina la mtumiaji.',
  loading: 'Inapakia…', empty: 'Hakuna data',
  amount: 'Kiasi', method: 'Njia ya malipo', date: 'Tarehe', receipt: 'Risiti',
  reference: 'Namba ya malipo', member: 'Mshiriki', church: 'Kanisa', total: 'Jumla',
  payments: 'Malipo', phone: 'Simu', email: 'Barua pepe', fullName: 'Majina kamili', language: 'Lugha',
  payTitle: 'Toa zaka', payerPhone: 'Namba ya simu ya malipo', payNow: 'Lipa sasa', paying: 'Inalipa…',
  paySuccess: 'Malipo yamepokelewa. Asante!', history: 'Historia ya malipo yangu',
  noHistory: 'Bado hujalipa zaka', receiptTitle: 'Risiti ya zaka', print: 'Chapisha', close: 'Funga',
  hello: 'Habari, {{name}}',
  members: 'Washiriki', addMember: 'Ongeza mshiriki', addMemberTitle: 'Mshiriki mpya', save: 'Hifadhi',
  memberAdded: 'Mshiriki ameongezwa. Nenosiri la awali ni 1234.', noMembers: 'Hakuna washiriki bado',
  reportTitle: 'Ripoti ya zaka', week: 'Wiki', month: 'Mwezi', year: 'Mwaka',
  previous: 'Iliyopita', next: 'Inayofuata', totalCollected: 'Jumla iliyokusanywa', paymentCount: 'Idadi ya malipo',
  cashTitle: 'Rekodi zaka ya fedha taslimu', searchMember: 'Tafuta mshiriki',
  searchPlaceholder: 'Jina au simu', selectMember: 'Chagua', recordCash: 'Rekodi malipo ya taslimu',
  cashRecorded: 'Malipo ya taslimu yamerekodiwa', allChurches: 'Makanisa yote', cashBadge: 'Taslimu',
  methods: { MTN_MOMO: 'MTN Mobile Money', AIRTEL_MONEY: 'Airtel Money', CARD: 'Kadi ya benki', BANK: 'Uhamisho wa benki', CASH: 'Fedha taslimu' },
  error: 'Kuna tatizo. Tafadhali jaribu tena.'
};

const en = {
  appName: 'SDA Tithe', logout: 'Log out',
  loginTitle: 'Sign in', loginSubtitle: 'Tithe management for SDA churches in Rwanda',
  identifier: 'Phone, email, church name/ID, or username', password: 'Password',
  signIn: 'Sign in', signingIn: 'Signing in…',
  loginHelp: 'Members: phone or email. Churches: name or ID. Tithe Manager: username.',
  loading: 'Loading…', empty: 'No data yet',
  amount: 'Amount', method: 'Payment method', date: 'Date', receipt: 'Receipt',
  reference: 'Reference', member: 'Member', church: 'Church', total: 'Total',
  payments: 'Payments', phone: 'Phone', email: 'Email', fullName: 'Full name', language: 'Language',
  payTitle: 'Pay tithe', payerPhone: 'Mobile money phone number', payNow: 'Pay now', paying: 'Paying…',
  paySuccess: 'Payment received. Thank you!', history: 'My tithe history',
  noHistory: "You haven't paid any tithe yet", receiptTitle: 'Tithe receipt', print: 'Print', close: 'Close',
  hello: 'Hello, {{name}}',
  members: 'Members', addMember: 'Add member', addMemberTitle: 'New member', save: 'Save',
  memberAdded: 'Member added. Initial password is 1234.', noMembers: 'No members yet',
  reportTitle: 'Tithe report', week: 'Week', month: 'Month', year: 'Year',
  previous: 'Previous', next: 'Next', totalCollected: 'Total collected', paymentCount: 'Number of payments',
  cashTitle: 'Record a cash tithe', searchMember: 'Find a member',
  searchPlaceholder: 'Name or phone', selectMember: 'Select', recordCash: 'Record cash payment',
  cashRecorded: 'Cash payment recorded', allChurches: 'All churches', cashBadge: 'Cash',
  methods: { MTN_MOMO: 'MTN Mobile Money', AIRTEL_MONEY: 'Airtel Money', CARD: 'Bank card', BANK: 'Bank transfer', CASH: 'Cash' },
  error: 'Something went wrong. Please try again.'
};

const fr = {
  appName: 'Dîme SDA', logout: 'Se déconnecter',
  loginTitle: 'Connexion', loginSubtitle: 'Gestion de la dîme pour les églises adventistes du Rwanda',
  identifier: "Téléphone, e-mail, nom/ID de l'église ou nom d'utilisateur", password: 'Mot de passe',
  signIn: 'Se connecter', signingIn: 'Connexion…',
  loginHelp: "Membres : téléphone ou e-mail. Églises : nom ou ID. Gestionnaire de la dîme : nom d'utilisateur.",
  loading: 'Chargement…', empty: 'Aucune donnée',
  amount: 'Montant', method: 'Mode de paiement', date: 'Date', receipt: 'Reçu',
  reference: 'Référence', member: 'Membre', church: 'Église', total: 'Total',
  payments: 'Paiements', phone: 'Téléphone', email: 'E-mail', fullName: 'Nom complet', language: 'Langue',
  payTitle: 'Payer la dîme', payerPhone: 'Numéro de téléphone du paiement', payNow: 'Payer maintenant', paying: 'Paiement…',
  paySuccess: 'Paiement reçu. Merci !', history: 'Mon historique de dîme',
  noHistory: 'Aucune dîme payée pour le moment', receiptTitle: 'Reçu de dîme', print: 'Imprimer', close: 'Fermer',
  hello: 'Bonjour, {{name}}',
  members: 'Membres', addMember: 'Ajouter un membre', addMemberTitle: 'Nouveau membre', save: 'Enregistrer',
  memberAdded: 'Membre ajouté. Mot de passe initial : 1234.', noMembers: 'Aucun membre pour le moment',
  reportTitle: 'Rapport de dîme', week: 'Semaine', month: 'Mois', year: 'Année',
  previous: 'Précédent', next: 'Suivant', totalCollected: 'Total collecté', paymentCount: 'Nombre de paiements',
  cashTitle: 'Enregistrer une dîme en espèces', searchMember: 'Rechercher un membre',
  searchPlaceholder: 'Nom ou téléphone', selectMember: 'Choisir', recordCash: 'Enregistrer le paiement en espèces',
  cashRecorded: 'Paiement en espèces enregistré', allChurches: 'Toutes les églises', cashBadge: 'Espèces',
  methods: { MTN_MOMO: 'MTN Mobile Money', AIRTEL_MONEY: 'Airtel Money', CARD: 'Carte bancaire', BANK: 'Virement bancaire', CASH: 'Espèces' },
  error: 'Une erreur est survenue. Veuillez réessayer.'
};

i18n
  .use(LanguageDetector)
  .use(initReactI18next)
  .init({
    resources: { rw: { translation: rw }, sw: { translation: sw }, en: { translation: en }, fr: { translation: fr } },
    fallbackLng: 'en',
    supportedLngs: ['rw', 'sw', 'en', 'fr'],
    interpolation: { escapeValue: false },
    detection: { order: ['localStorage', 'navigator'], caches: ['localStorage'] }
  });

export default i18n;
