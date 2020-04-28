

package be.uantwerpen.labplanner.Service;


import java.util.List;
import java.util.Optional;

import be.uantwerpen.labplanner.Model.OwnTag;
import be.uantwerpen.labplanner.Repository.OwnTagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OwnTagService {
    @Autowired
    private OwnTagRepository tagRepository;

    public OwnTagService() {
    }

    public List<OwnTag> findAll() {
        return this.tagRepository.findAll();
    }

    public Optional<OwnTag> findById(Long id) {
        return this.tagRepository.findById(id);
    }

    public Optional<OwnTag> findByName(String name) {
        return this.tagRepository.findByName(name);
    }

    public OwnTag save(OwnTag tag) {
        return (OwnTag)this.tagRepository.save(tag);
    }

    public void delete(OwnTag tag) {
        this.tagRepository.delete(tag);
    }

    public Boolean deleteById(Long id) {
        if (this.exists(id)) {
            this.tagRepository.deleteById(id);
            return !this.exists(id);
        } else {
            return false;
        }
    }

    private Boolean exists(Long id) {
        OwnTag tag = (OwnTag)this.tagRepository.findById(id).orElse((OwnTag)null);
        return tag != null;
    }
}
