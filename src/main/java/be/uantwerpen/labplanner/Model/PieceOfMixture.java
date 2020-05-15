package be.uantwerpen.labplanner.Model;


import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class PieceOfMixture extends AbstractPersistable<Long> {
    @OneToOne
    @JoinColumn(name = "mixture")
    private Mixture mixture;

    @Column(name = "mixtureComment")
    private String mixtureComment;

    @Column(name = "mixtureAmount")
    private double mixtureAmount;


    public PieceOfMixture() {
    }

    public PieceOfMixture(Mixture mixture, String mixtureComment, double mixtureAmount) {
        this.mixture=mixture;
        this.mixtureComment=mixtureComment;
        this.mixtureAmount=mixtureAmount;
    }

    @Override
    public Long getId() {
        return super.getId();
    }

    @Override
    public void setId(Long id) {
        super.setId(id);
    }

    public Mixture getMixture() {
        return mixture;
    }

    public void setMixture(Mixture mixture) {
        this.mixture = mixture;
    }

    public String getMixtureComment() {
        return mixtureComment;
    }

    public void setMixtureComment(String mixtureComment) {
        this.mixtureComment = mixtureComment;
    }

    public double getMixtureAmount() {
        return mixtureAmount;
    }

    public void setMixtureAmount(double mixtureAmount) {
        this.mixtureAmount = mixtureAmount;
    }
}