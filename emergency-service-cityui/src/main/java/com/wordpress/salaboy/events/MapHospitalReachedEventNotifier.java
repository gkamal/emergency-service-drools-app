/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wordpress.salaboy.events;

import com.wordpress.salaboy.CityEntitiesUtils;
import com.wordpress.salaboy.EmergencyService;
import java.util.Iterator;
import org.drools.runtime.rule.QueryResults;
import org.drools.runtime.rule.QueryResultsRow;
import com.wordpress.salaboy.model.Hospital;
import com.wordpress.salaboy.model.Patient;
import com.wordpress.salaboy.model.events.PatientAtTheHospitalEvent;
import com.wordpress.salaboy.services.AmbulanceMonitorService;
import com.wordpress.salaboy.ui.CityMapUI;

/**
 *
 * @author salaboy
 */
public class MapHospitalReachedEventNotifier implements WorldEventNotifier {

    private Long hospitalId;

    public MapHospitalReachedEventNotifier(Long hospitalId) {
        this.hospitalId = hospitalId;
    }

    @Override
    public void notify(NotifierEvent event) {
        Long ambulanceId = ((HospitalReachedNotifierEvent)event).getAmbulanceId();

        AmbulanceMonitorService.getInstance().stop();
        EmergencyService.getInstance().sendPatientAtTheHospitalEvent(new PatientAtTheHospitalEvent(), ambulanceId);



        QueryResults results = EmergencyService.getInstance().getQueryResults("getPatient");
        Iterator<QueryResultsRow> it = results.iterator();
        Patient patient = null;
        while (it.hasNext()) {
            Object o = it.next().get(results.getIdentifiers()[0]);
            patient = (Patient) o;
        }
        Hospital hospital = CityEntitiesUtils.getHospitalById(hospitalId);
        hospital.addPatient(patient);
        CityMapUI.getInstance().removeHospital(hospitalId);
    }
}
