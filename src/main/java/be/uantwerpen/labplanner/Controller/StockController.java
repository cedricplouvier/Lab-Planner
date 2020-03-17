package be.uantwerpen.labplanner.Controller;


import be.uantwerpen.labplanner.common.model.stock.Product;
import be.uantwerpen.labplanner.common.model.stock.Tag;
import be.uantwerpen.labplanner.common.model.stock.Unit;
import be.uantwerpen.labplanner.common.service.stock.ProductService;
import be.uantwerpen.labplanner.common.service.stock.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Controller
public class StockController {

    @Autowired
    private ProductService productService;
    @Autowired
    private TagService tagService;

    //Populate
    @ModelAttribute("allProducts")
    public Iterable<Product> populateProducts(){
        return  this.productService.findAll();
    }


    //Mappings
    //@PreAuthorize("hasAuthority('Stock - Modify - All') or hasAuthority('Stock - Aggregates + Bitumen Read only - Basic') or hasAuthority('Stock - Aggregates + Bitumen Read only - Advanced') or hasAuthority('Stock - Aggregates + Bitumen Modify - Advanced')")
    @RequestMapping(value="/products", method= RequestMethod.GET)
    public String showProducts(final ModelMap model){
        //aggregates + bitumen
        List<Product> agg_bit = new ArrayList<>();
        //consumables + other
        List<Product> con_oth = new ArrayList<>();
        Tag aggregateTag = null;
        Tag bitumenTag = null;
        Optional<Tag> OptAggregateTag = tagService.findByName("Aggregates");
        Optional<Tag> optBitumenTag = tagService.findByName("Bitumen");
        if(OptAggregateTag.isPresent()){
             aggregateTag = OptAggregateTag.get();
        }
        if(optBitumenTag.isPresent()){
             bitumenTag = optBitumenTag.get();
        }




        List<Product> products = productService.findAll();
        Iterator<Product> it = products.iterator();
        while (it.hasNext()) {
            Product temp = it.next();
            if(temp.getTags().contains(aggregateTag) || temp.getTags().contains(bitumenTag)){
                agg_bit.add(temp);
            }
            else {
                con_oth.add(temp);
            }
        }
        model.addAttribute("agg_bit", agg_bit);
        model.addAttribute("con_oth", con_oth);
        return "/Stock/products-list";
    }



    @PreAuthorize("hasAuthority('Stock - Modify - All')")
    @RequestMapping(value="/products/put", method= RequestMethod.GET)
    public String viewCreateProducts(final ModelMap model){
        model.addAttribute("allProducts", productService.findAll());
        model.addAttribute("allTags", tagService.findAll());
        model.addAttribute("product",new Product("","",0.0, 0.0, 0.0, 0.0, null, "URL", "", null,null, LocalDateTime.now(), LocalDateTime.now(), null));
        model.addAttribute("units",Unit.values());
        return "/Stock/products-manage";
    }

    @PreAuthorize("hasAuthority('Stock - Modify - All')")
    @RequestMapping(value="/products/{id}", method= RequestMethod.GET)
    public String viewEditProduct(@PathVariable Long id, final ModelMap model){
        model.addAttribute("allProducts", productService.findAll());
        model.addAttribute("allTags", tagService.findAll());
        model.addAttribute("product",productService.findById(id).orElse(null));
        model.addAttribute("units", Unit.values());
        return "/Stock/products-manage";
    }

    @PreAuthorize("hasAuthority('Stock - Modify - All')")
    @RequestMapping(value={"/products/", "/products/{id}"},
            method= RequestMethod.POST)
    public String addProduct(@Valid Product product, BindingResult result,
                          final ModelMap model){
        if(result.hasErrors()){
            model.addAttribute("allTags", tagService.findAll());
            model.addAttribute("units",Unit.class);

            return "/Stock/products-manage";
        }
        productService.save(product);
        return "redirect:/products";
    }

    @PreAuthorize("hasAuthority('Stock - Modify - All')")
    @RequestMapping(value="/products/{id}/delete",method = RequestMethod.GET)
    public String deleteProduct(@PathVariable Long id, final ModelMap model){
        productService.deleteById(id);
        model.clear();
        return "redirect:/products";
    }

    @RequestMapping(value ="/products/info/{id}", method= RequestMethod.GET)
    public String viewProductInfo(@PathVariable Long id, final ModelMap model){
        model.addAttribute("product",productService.findById(id).orElse(null));
        model.addAttribute("allTags", tagService.findAll());
        return "/Stock/products-info";
    }



    @PreAuthorize("hasAuthority('Stock - Modify - All')")
    @RequestMapping(value="/tags", method = RequestMethod.GET)
    public String showProductTags(final ModelMap model){
        model.addAttribute("allProductTags",tagService.findAll());
        return "/Tags/tags-list";
    }

    @PreAuthorize("hasAuthority('Stock - Modify - All')")
    @RequestMapping(value="/tags/{id}/delete",method = RequestMethod.GET)
    public String deleteTag(@PathVariable Long id, final ModelMap model){
        tagService.deleteById(id);
        model.clear();
        return "redirect:/tags";
    }

    @PreAuthorize("hasAuthority('Stock - Modify - All')")
    @RequestMapping(value="/tags/{id}", method= RequestMethod.GET)
    public String viewEditTag(@PathVariable Long id, final ModelMap model){
        model.addAttribute("allTags", tagService.findAll());
        model.addAttribute("tag",tagService.findById(id).orElse(null));
        return "/Tags/tags-manage";
    }

    @PreAuthorize("hasAuthority('Stock - Modify - All')")
    @RequestMapping(value={"/tags", "/tags/{id}"},
            method= RequestMethod.POST)
        public String addTag(@Valid Tag tag, BindingResult result,
                             final ModelMap model){
        if(result.hasErrors()){
            model.addAttribute("allTags", tagService.findAll());
            return "Tags/tags-manage";
        }
        tagService.save(tag);
        return "redirect:/tags";
    }

    @PreAuthorize("hasAuthority('Stock - Modify - All')")
    @RequestMapping(value="/tags/put", method= RequestMethod.GET)
    public String viewCreateTags(final ModelMap model){
        model.addAttribute("allTags", tagService.findAll());
        model.addAttribute("tag", new Tag(""));
        return "/Tags/tags-manage";
    }

    @RequestMapping(value="/mixtures", method= RequestMethod.GET)
    public String viewMixtures(final ModelMap model){
        model.addAttribute("allTags", tagService.findAll());
        model.addAttribute("tag", new Tag(""));
        return "/Mixtures/mixtures-list";
    }
}