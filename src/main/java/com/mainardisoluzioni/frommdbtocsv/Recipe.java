/*
 * Copyright (C) 2025 Davide Mainardi <davide at mainardisoluzioni.com>.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mainardisoluzioni.frommdbtocsv;

/**
 *
 * @author Davide Mainardi <davide at mainardisoluzioni.com>
 */
public class Recipe {
    /**
     * Column name 'Descrizione'
     */
    private final String codice;
    private final Integer tipo;
    private final Double altezza;
    private final Double passo;
    private final Double avanzamento;
    private final Double avanzamento2;
    private final Integer strati;
    private final Double delta1;
    private final Double correzione;
    private final Double offsetZ;

    public Recipe(String codice, Integer tipo, Double altezza, Double passo, Double avanzamento, Double avanzamento2, Integer strati, Double delta1, Double correzione, Double offsetZ) {
        this.codice = codice;
        this.tipo = tipo;
        this.altezza = altezza;
        this.passo = passo;
        this.avanzamento = avanzamento;
        this.avanzamento2 = avanzamento2;
        this.strati = strati;
        this.delta1 = delta1;
        this.correzione = correzione;
        this.offsetZ = offsetZ;
    }

    public String getCodice() {
        return codice;
    }

    public Integer getTipo() {
        return tipo;
    }

    public Double getAltezza() {
        return altezza;
    }

    public Double getPasso() {
        return passo;
    }

    public Double getAvanzamento() {
        return avanzamento;
    }

    public Double getAvanzamento2() {
        return avanzamento2;
    }

    public Integer getStrati() {
        return strati;
    }

    public Double getDelta1() {
        return delta1;
    }

    public Double getCorrezione() {
        return correzione;
    }

    public Double getOffsetZ() {
        return offsetZ;
    }
    
}
