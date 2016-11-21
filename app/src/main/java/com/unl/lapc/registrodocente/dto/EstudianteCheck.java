package com.unl.lapc.registrodocente.dto;

import com.unl.lapc.registrodocente.modelo.Estudiante;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jaja on 20/11/2016.
 */

public class EstudianteCheck {

    private Estudiante estudiante;
    private boolean checked;

    public  EstudianteCheck(Estudiante e){
        this.estudiante = e;
    }

    public  EstudianteCheck(Estudiante e, boolean chk){
        this.estudiante = e;
        this.checked = chk;
    }

    public Estudiante getEstudiante() {
        return estudiante;
    }

    public void setEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public  static List<EstudianteCheck> getCheckList(List<Estudiante> list, List<EstudianteCheck> old){
        List<EstudianteCheck> r = new ArrayList<>();
        for(Estudiante e: list){
            boolean checked = false;

            for(EstudianteCheck c: old){
                if(c.getEstudiante().getId() == e.getId()){
                    checked = true;
                }
            }

            r.add(new EstudianteCheck(e, checked));
        }
        return  r;
    }
}
