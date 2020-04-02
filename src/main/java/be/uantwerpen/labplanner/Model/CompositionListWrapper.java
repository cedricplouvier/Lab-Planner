package be.uantwerpen.labplanner.Model;

import java.util.List;


public class CompositionListWrapper {
    public List<Composition> compositionList;

    public void addComposition(Composition comp){
        this.compositionList.add(comp);

    }

    public List<Composition> getCompositionList() {
        return compositionList;
    }

    public void setCompositionList(List<Composition> compositionList) {
        this.compositionList = compositionList;
    }
}
