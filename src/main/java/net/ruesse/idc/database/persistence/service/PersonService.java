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

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Named;
import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.Query;
import static net.ruesse.idc.control.ApplicationControlBean.getPersistenceParameters;
import net.ruesse.idc.control.Constants;
import net.ruesse.idc.database.persistence.Person;

/**
 *
 * @author Ulrich Rüße <ulrich@ruesse.net>
 */
@ApplicationScoped
@Named
public class PersonService implements Serializable {

    private final static Logger LOGGER = Logger.getLogger(PersonService.class.getName());
    EntityManager em = Persistence.createEntityManagerFactory(Constants.PERSISTENCE_UNIT_NAME, getPersistenceParameters()).createEntityManager();
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    public PersonService() {
        LOGGER.setLevel(Level.FINE);
    }

    private final DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
    public static List<Person> personlist = null;

    /**
     *
     * @param string
     * @param delimiter
     * @return
     */
    public static String substringAfter(String string, String delimiter) {
        int pos = string.indexOf(delimiter);

        return pos >= 0 ? string.substring(pos + delimiter.length()) : "";
    }

    /**
     * 
     * @return 
     */
    public List<Person> createPersons() {

        LOGGER.log(Level.FINE, "Starte Query auf Personen");
        Query q = em.createNamedQuery("Person.findAll");
        personlist = q.getResultList();
        /*
         pl.foreach((p) -> {
            LOGGER.log(Level.FINE, "mnr: {0}", p.getMglnr());
            personlist.add(new PersonUser(p));
        })

        LOGGER.log(Level.FINE, "VorForEach");
        for (Person p : pl) {
            LOGGER.log(Level.FINE, "mnr: {0}", p.getMglnr());
            personlist.add(new PersonUser(p));
        }
         */
        return personlist;
    }

    /**
     *
     * @param scantext
     * @return
     */
    public Person findPerson(String scantext) {
        Person p;
        String mnr;
        p = null;

        if (scantext != null) {
            mnr = scantext.replaceAll(" ", "");
            LOGGER.log(Level.FINE, "1 mnr: {0}", mnr);

            if (mnr.length() != 13) {
                mnr = substringAfter(scantext, "=");
            }
            LOGGER.log(Level.FINE, "2 mnr: {0}", mnr);

            if (personlist == null) {
                createPersons();
            }

            if (personlist != null && mnr.length() == 13) {
                for (Person pr : personlist) {
                    if (pr.getMglnr().equals(mnr)) {
                        p = pr;
                    }
                }
            }
        }
        return p;
    }
}
