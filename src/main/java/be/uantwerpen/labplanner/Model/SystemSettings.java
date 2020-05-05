package be.uantwerpen.labplanner.Model;

import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class SystemSettings extends AbstractPersistable<Long> {

    private static SystemSettings currentSystemSettings = new SystemSettings();

    public static SystemSettings getCurrentSystemSettings() {
        return currentSystemSettings;
    }

    public static void setCurrentSystemSettings(SystemSettings currentSystemSettings) {
        SystemSettings.currentSystemSettings = currentSystemSettings;
    }

    @OneToOne
    @JoinColumn(name = "officeHours", unique = true, nullable = false)
    private OfficeHours officeHours;

   public SystemSettings() {
       this.officeHours = new OfficeHours();
   }

   public SystemSettings(OfficeHours officeHours) {
        this.officeHours = officeHours;
    }

    public OfficeHours getCurrentOfficeHours() {
        return officeHours;
    }

    public void setCurrentOfficeHours(OfficeHours officeHours) {
        this.officeHours = officeHours;
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
