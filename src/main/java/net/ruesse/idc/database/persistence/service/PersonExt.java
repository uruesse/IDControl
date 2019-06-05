/*
 * Copyright 2019 Ulrich Rüße <ulrich@ruesse.net>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.ruesse.idc.database.persistence.service;

import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;
import static net.ruesse.idc.control.ApplicationControlBean.getPersistenceParameters;
import net.ruesse.idc.control.Constants;
import net.ruesse.idc.control.ControlBean;
import net.ruesse.idc.database.persistence.Beitrag;
import net.ruesse.idc.database.persistence.Card;
import net.ruesse.idc.database.persistence.Cv;
import net.ruesse.idc.database.persistence.Family;
import net.ruesse.idc.database.persistence.Offenerechnungen;
import net.ruesse.idc.database.persistence.Person;

/**
 *
 * @author Ulrich Rüße <ulrich@ruesse.net>
 */
@ManagedBean
@ViewScoped
public class PersonExt {

    private final static Logger LOGGER = Logger.getLogger(ControlBean.class.getName());
    EntityManager em = Persistence.createEntityManagerFactory(Constants.PERSISTENCE_UNIT_NAME, getPersistenceParameters()).createEntityManager();

    static final long MSPERYEAR = ((long) 365 * 24 * 60 * 60 * 1000);

    public Person person;
    public Card card = null;

    private boolean mitarbeiter;
    private int userstatus = 0;
    private boolean wassergeld;

    /**
     *
     * @return
     */
    public Card getCard() {
        // Lazy load
        if (card == null) {
            readCard();
        }
        return card;
    }

    /**
     *
     * @param card
     */
    public void setCard(Card card) {
        this.card = card;
    }

    /**
     * Quick and Dirty TODO umfassend überarbeiten!
     */
    private void readCard() {
        Card c;
        try {
            c = (Card) em.createNamedQuery("Card.findByMglnr")
                    .setParameter("mglnr", person.getMglnr())
                    .getSingleResult();
            LOGGER.info ("Cardinfo: "+c.getPrfmglnr());
        } catch (NoResultException e) {
            // einfach einen leeren Datensatz erzeugen, wenn nix gefunden wird
            c = new Card();
        }
        setCard(c);
    }

    /**
     *
     * @return
     */
    public boolean isMitarbeiter() {
        return mitarbeiter;
    }

    /**
     *
     * @param mitarbeiter
     */
    public void setMitarbeiter(boolean mitarbeiter) {
        this.mitarbeiter = mitarbeiter;
    }

    public int getUserstatus() {
        return userstatus;
    }

    public void setUserstatus(int userstatus) {
        this.userstatus = userstatus;
    }

    /**
     *
     * @return
     */
    public boolean isWassergeld() {
        return wassergeld;
    }

    /**
     *
     * @param wassergeld
     */
    public void setWassergeld(boolean wassergeld) {
        this.wassergeld = wassergeld;
    }

    /**
     *
     * @param person
     */
    public PersonExt(Person person) {
        this.person = person;
        LOGGER.setLevel(Level.INFO);
        if (person != null) {
            checkMitarbeiterStatus();
            checkWassergeldStatus();
        }
    }

    /**
     *
     * @return
     */
    public Person getPerson() {
        return this.person;
    }

    /**
     *
     * @return
     */
    public String getFullname() {
        return person.getVorname() + " " + person.getNachname();
    }

    /**
     *
     * @return
     */
    public String getStrMglnr() {
        return String.format("%013d", person.getMglnr());
    }

    /**
     *
     * @param mglnr
     * @return
     */
    public String formatMglnr(long mglnr) {
        String str = String.format("%013d", mglnr);
        return str.substring(0, 7) + " " + str.substring(7, 8) + " " + str.substring(8, 13);
    }

    /**
     *
     * @return
     */
    public String formatMglnr() {
        String str = getStrMglnr();
        return str.substring(0, 7) + " " + str.substring(7, 8) + " " + str.substring(8, 13);
    }

    /**
     *
     * @return
     */
    public String getStrFMglnr() {
        return formatMglnr(person.getMglnr().longValue());
    }

    /**
     *
     * @return
     */
    public String getStrFNr() {
        Collection<Person> family;
        if (person.getFnr() != null) {
            return (person.getFnr()).getFnr().toString();
        } else {
            return null;
        }
    }

    /**
     *
     * @return
     */
    public boolean isAustritt() {
        return person.getAustritt() != null;
    }

    /**
     *
     * @return
     */
    public boolean isRechnung() {
        Collection<Offenerechnungen> or;
        or = person.getOffenerechnungenCollection();
        return !or.isEmpty();
    }

    /**
     *
     * @return
     */
    public boolean isBeitrag() {
        Collection<Beitrag> be;
        be = person.getBeitragCollection();
        return !be.isEmpty();
    }

    /**
     * Berechnet das aktuelle Alter ACHTUNG macht keine Prüfung ob
     * eingangsparameter valide ist.
     *
     * @param d
     * @return
     */
    private int calcAge(Date d) {
        Date now = new Date();
        return (int) ((now.getTime() - d.getTime()) / MSPERYEAR);
    }

    /**
     *
     * @return
     */
    public boolean isGeburtsdatum() {
        return person.getGeburtsdatum() != null;
    }

    /**
     *
     * @return
     */
    public int getAge() {
        long age = 0;
        if (person.getGeburtsdatum() != null) {
            age = calcAge(person.getGeburtsdatum());
            LOGGER.log(Level.FINE, "Alter: {0}", age);
        }
        return (int) age;
    }

    /**
     *
     * @return
     */
    public String getSortAge() {
        //Irgendwann mal die führenden nullen durch &nbsp; ersetzten. Das wird aber per default angeszeigt und ansonsten nicht richtig sortiert.

        if (person.getGeburtsdatum() != null) {
            return String.format("%03d", calcAge(person.getGeburtsdatum()));
        }
        return null;
    }

    /**
     *
     * @return
     */
    public String getStrAge() {
        if (person.getGeburtsdatum() != null) {
            return String.format("%d", calcAge(person.getGeburtsdatum()));
        }
        return null;
    }

    /**
     *
     * @return
     */
    public boolean isBemerkung() {
        return person.getBemerkung() != null;
    }

    /**
     *
     * @return
     */
    public boolean isFamilie() {
        return person.getFnr() != null;
    }

    /**
     *
     * @return
     */
    public boolean isLastschrift() {
        return "Lastschrift".equals(person.getZahlungsmodus());
    }

    /**
     *
     */
    private void checkMitarbeiterStatus() {
        LOGGER.log(Level.FINE, "Status: {0}", person.getStatus());
        if ("Aktiv".equals(person.getStatus())) {
            Date now = new Date();
            /* suche Mitarbeiter im aktuellen Lebenslaufeintrag */
            Collection<Cv> cv = person.getCvCollection();
            try {
                for (Cv c : cv) {
                    LOGGER.log(Level.FINE, "c: {0} {1} {2} {3}", new Object[]{c.getCvkey(), c.getCvvalue(), c.getValidfrom(), c.getValidto()});
                    if ("Funktionen".equals(c.getCvkey())) {
                        if (now.after(c.getValidfrom()) && (c.getValidto() == null || now.before(c.getValidto()))) {
                            setMitarbeiter(true);
                            if (c.getCvvalue().toLowerCase().equals("mitgliederverwaltung")) {
                                setUserstatus(1);
                                String txt = c.getKurztext().toLowerCase();
                                if (txt.contains("einlasskontrolle")) {
                                    setUserstatus(2);
                                }
                                if (txt.contains("sewobe")) {
                                    setUserstatus(3);
                                }
                                if (txt.contains("admin")) {
                                    setUserstatus(4);
                                }
                                if (txt.contains("dev")) {
                                    setUserstatus(5);
                                }
                            }
                            LOGGER.log(Level.INFO, "Mitarbeiterstatus=true: Userstatus={0} CV: {1} {2} {3} {4} {5}", new Object[]{getUserstatus(), c.getCvkey(), c.getCvvalue(), c.getKurztext(), c.getValidfrom(), c.getValidto()});

                        }
                    }
                }
            } catch (java.util.NoSuchElementException e) {
            }
        }
    }

    /**
     *
     */
    private void checkWassergeldStatus() {
        Date now = new Date();
        Collection<Beitrag> be = person.getBeitragCollection();
        for (Beitrag b : be) {
            LOGGER.log(Level.FINE, "b: {0} {1} {2}", new Object[]{b.getBeitragsposition(), b.getFaelligStart(), b.getFaelligEnde()});
            if ("Wassergeld".equals(b.getBeitragsposition())) {
                if (now.getYear() == b.getFaelligStart().getYear() || (now.getYear() + 1) == b.getFaelligStart().getYear() && now.getYear() < b.getFaelligEnde().getYear()) {
                    setWassergeld(true);
                }
            }
        }
    }

    /**
     *
     * @return
     */
    public String getState() {
        LOGGER.log(Level.FINE, "Status: {0}", person.getStatus());
        if (isMitarbeiter()) {
            return "Mitarbeiter";
        } else if (isWassergeld()) {
            return "aktiv";
        } else {
            return "inaktiv";
        }
    }

    /**
     *
     * @return
     */
    public Person getFremdzahlerPerson() {

        Person fz;

        Query q = em.createNamedQuery("Person.findByMglnr");
        q.setParameter("mglnr", person.getFremdzahler());
        try {
            fz = (Person) q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }

        return fz;
    }

    /**
     *
     * @return
     */
    public String getFremdzahlerInfo() {
        String str = "Fremdzahler unbekannt";
        Person fz = getFremdzahlerPerson();
        if (fz != null) {
            str = "[";
            str += formatMglnr(fz.getMglnr().longValue());
            str += "] " + fz.getVorname() + " " + fz.getNachname();
        }
        return str;
    }

    /**
     *
     * @return
     */
    public String getOpenbills() {
        String str = "";
        int summe = 0;
        int count = 0;
        int lastschriften = 0;

        Collection<Offenerechnungen> openbills = null;

        Collection<Person> family;
        if (person.getFnr() != null) {
            Family f = person.getFnr();
            family = f.getPersonCollection();
        } else {
            family = new ArrayList() {
                {
                    add(person);
                }
            };
        }

        for (Person p : family) {
            if (p.getAbweichenderzahler() || p.getFremdzahler() != null) {
                Person fz = getFremdzahlerPerson();

                if (fz != null) {
                    if (openbills == null) {
                        openbills = fz.getOffenerechnungenCollection();
                    } else {
                        openbills.addAll(fz.getOffenerechnungenCollection());
                    }
                }
            } else {
                if (openbills == null) {
                    openbills = p.getOffenerechnungenCollection();
                } else {
                    openbills.addAll(p.getOffenerechnungenCollection());
                }
            }
        }

        if (openbills != null) {
            for (Offenerechnungen ob : openbills) {
                LOGGER.log(Level.FINE, "offene Rechnungen: {0} {1} {2}", new Object[]{ob.getRechnungsnummer(), ob.getRechnungssumme(), ob.getRechnungsdatum()});
                summe += ob.getRechnungssumme();
                //str += " [" + ob.getRechnungsnummer() + "]";
                count += 1;
                if ("Lastschrift".equals(ob.getZahlmodus())) {
                    lastschriften += ob.getRechnungssumme();
                }
            }

            if (summe > 0) {
                if (count == 1) {
                    str = count + " offene Rechnung über ";
                } else {
                    str = count + " offene Rechnungen über insgesamt ";
                }
                str += String.format("%,.2f", (double) summe / 100);
                str += " €";
                if (lastschriften == summe) {
                    str += ", Gesamtbetrag wird abgebucht";
                } else if (lastschriften > 0) {
                    str += ", davon werden ";
                    str += String.format("%,.2f", (double) lastschriften / 100);
                    str += " € abgebucht.";
                }
            }
        }
        return str;
    }

}
