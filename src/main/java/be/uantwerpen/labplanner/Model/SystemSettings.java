package be.uantwerpen.labplanner.Model;

import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class SystemSettings extends AbstractPersistable<Long> {

    @OneToOne
    @JoinColumn(name = "currentOfficeHours", unique = true, nullable = false)
    private OfficeHours currentOfficeHours;

   public SystemSettings() {
   }

   public SystemSettings(OfficeHours currentOfficeHours) {
        this.currentOfficeHours = currentOfficeHours;
    }

    public OfficeHours getCurrentOfficeHours() {
        return currentOfficeHours;
    }

    public void setCurrentOfficeHours(OfficeHours currentOfficeHours) {
        this.currentOfficeHours = currentOfficeHours;
    }

    @Override
    public Long getId() {
        return super.getId();
    }

    @Override
    protected void setId(Long id) {
        super.setId(id);
    }
}
