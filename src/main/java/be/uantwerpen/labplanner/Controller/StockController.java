package be.uantwerpen.labplanner.Controller;


import be.uantwerpen.labplanner.Model.Composition;
import be.uantwerpen.labplanner.Model.Mixture;
import be.uantwerpen.labplanner.Service.CompositionService;
import be.uantwerpen.labplanner.Service.MixtureService;
import be.uantwerpen.labplanner.common.model.stock.Product;
import be.uantwerpen.labplanner.common.model.stock.Tag;
import be.uantwerpen.labplanner.common.model.stock.Unit;
import be.uantwerpen.labplanner.common.service.stock.ProductService;
import be.uantwerpen.labplanner.common.service.stock.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
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
import java.util.*;

@Controller
public class StockController {

    @Autowired
    private ProductService productService;
    @Autowired
    private TagService tagService;
    @Autowired
    private MixtureService mixtureService;
    @Autowired
    private CompositionService compositionService;

    //Populate
    @ModelAttribute("allProducts")
    public Iterable<Product> populateProducts(){
        return  this.productService.findAll();
    }

    public List<Product> getAggBitList(){
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
        return agg_bit;
    }

    public List<Product> getComOthList(){
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
        return con_oth;
    }

    //Mappings
    //@PreAuthorize("hasAuthority('Stock - Modify - All') or hasAuthority('Stock - Aggregates + Bitumen Read only - Basic') or hasAuthority('Stock - Aggregates + Bitumen Read only - Advanced') or hasAuthority('Stock - Aggregates + Bitumen Modify - Advanced')")
    @RequestMapping(value="/products", method= RequestMethod.GET)
    public String showProducts(final ModelMap model){
        List<Product> agg_bit = getAggBitList();
        List<Product> con_oth = getComOthList();

        model.addAttribute("agg_bit", agg_bit);
        model.addAttribute("con_oth", con_oth);
        return "/Stock/products-list";
    }



    @PreAuthorize("hasAuthority('Stock - Modify - All') or hasAuthority('Stock - Aggregates + Bitumen Modify - Advanced')")
    @RequestMapping(value="/products/put", method= RequestMethod.GET)
    public String viewCreateProducts(final ModelMap model){
        model.addAttribute("allProducts", productService.findAll());
        model.addAttribute("allTags", tagService.findAll());
        model.addAttribute("product",new Product("","",0.0, 0.0, 0.0, 0.0, null, "URL", "", null,null, LocalDateTime.now(), LocalDateTime.now(), null));
        model.addAttribute("units",Unit.values());
        return "/Stock/products-manage";
    }

    @PreAuthorize("hasAuthority('Stock - Modify - All') or hasAuthority('Stock - Aggregates + Bitumen Modify - Advanced')")
    @RequestMapping(value="/products/{id}", method= RequestMethod.GET)
    public String viewEditProduct(@PathVariable Long id, final ModelMap model){
        model.addAttribute("allProducts", productService.findAll());
        model.addAttribute("allTags", tagService.findAll());
        model.addAttribute("product",productService.findById(id).orElse(null));
        model.addAttribute("units", Unit.values());
        return "/Stock/products-manage";
    }

    @PreAuthorize("hasAuthority('Stock - Modify - All') or hasAuthority('Stock - Aggregates + Bitumen Modify - Advanced')")
    @RequestMapping(value={"/products/", "/products/{id}"},
            method= RequestMethod.POST)
    public String addProduct(@Valid Product product, BindingResult result,
                          final ModelMap model){
        List<Product > products = productService.findAll();
        Iterator<Product> it = products.iterator();
        String NameIsUsed = null;
        while (it.hasNext()) {
            Product temp = it.next();
            if(temp.getName().contains(product.getName()) && !temp.getId().equals(product.getId())){
                NameIsUsed = "There is already a product with the name " + product.getName();
            }
        }

        if(product.getName().length() == 0 ){
            model.addAttribute("allTags", tagService.findAll());
            model.addAttribute("errormessage", "Please give the product a valid name.");
            model.addAttribute("units", Unit.values());

            return "/Stock/products-manage";
        }

        if(product.getDescription().length() == 0 ){
            model.addAttribute("allTags", tagService.findAll());
            model.addAttribute("errormessage", "Please give the product a valid description.");
            model.addAttribute("units", Unit.values());

            return "/Stock/products-manage";
        }

        if(product.getProperties().length() == 0 ){
            model.addAttribute("allTags", tagService.findAll());
            model.addAttribute("errormessage", "Please give the product a valid properties description.");
            model.addAttribute("units", Unit.values());

            return "/Stock/products-manage";
        }

        if(product.getStockLevel() < 0){
            model.addAttribute("allTags", tagService.findAll());
            model.addAttribute("errormessage", "The stock level is not valid.");
            model.addAttribute("units", Unit.values());

            return "/Stock/products-manage";
        }

        if(product.getLowStockLevel() < 0){
            model.addAttribute("allTags", tagService.findAll());
            model.addAttribute("errormessage", "The low stock level is not valid.");
            model.addAttribute("units", Unit.values());

            return "/Stock/products-manage";
        }

        if(product.getReservedStockLevel() < 0){
            model.addAttribute("allTags", tagService.findAll());
            model.addAttribute("errormessage", "The reserved stock level is not valid.");
            model.addAttribute("units", Unit.values());

            return "/Stock/products-manage";
        }

        if(product.getTags().size() == 0){
            model.addAttribute("allTags", tagService.findAll());
            model.addAttribute("errormessage", "Please assign a tag to the product");
            model.addAttribute("units", Unit.values());

            return "/Stock/products-manage";
        }

        if(NameIsUsed != null){
            model.addAttribute("allTags", tagService.findAll());
            model.addAttribute("errormessage", NameIsUsed);
            model.addAttribute("units", Unit.values());

            return "/Stock/products-manage";
        }

        if(result.hasErrors()){
            model.addAttribute("allTags", tagService.findAll());
            model.addAttribute("errormessage", "Oops, something went wrong!");
            model.addAttribute("units", Unit.values());

            return "/Stock/products-manage";
        }
        productService.save(product);
        return "redirect:/products";
    }

    @PreAuthorize("hasAuthority('Stock - Modify - All')")
    @RequestMapping(value="/products/{id}/delete",method = RequestMethod.GET)
    public String deleteProduct(@PathVariable Long id, final ModelMap model){
        Locale current = LocaleContextHolder.getLocale();
        List<Composition> compositions = compositionService.findAll();
        Product product = null;
        Optional<Product> tempProd = productService.findById(id);
        boolean isUsed = false;
        if(tempProd.isPresent()){
            product = tempProd.get();
        }
        Iterator<Composition> it = compositions.iterator();
        while (it.hasNext()) {
            Composition temp = it.next();
            if(temp.getProduct().equals(product)){
                isUsed = true;
            }
        }
        if (isUsed){
            List<Product> agg_bit = getAggBitList();
            List<Product> con_oth = getComOthList();
            model.addAttribute("agg_bit", agg_bit);
            model.addAttribute("con_oth", con_oth);
            model.addAttribute("error", ResourceBundle.getBundle("messages",current).getString("product.deleteError"));
            return "/Stock/products-list";
        }
        else {
            productService.deleteById(id);
            model.clear();
            return "redirect:/products";
        }



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
        Locale current = LocaleContextHolder.getLocale();
        List<Product> products = productService.findAll();
        Tag tag = null;
        Optional<Tag> tempTag = tagService.findById(id);
        boolean isUsed = false;
        if(tempTag.isPresent()){
            tag = tempTag.get();
        }

        Iterator<Product> it = products.iterator();
        while (it.hasNext()) {
            Product temp = it.next();
            if(temp.getTags().contains(tag)){
                isUsed = true;
            }
        }
        if (isUsed){
            model.addAttribute("allProductTags",tagService.findAll());
            model.addAttribute("error", "Please give the tag a valid name");
            return "/Tags/tags-list";
        }
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
        List<Tag > tags = tagService.findAll();
        Iterator<Tag> it = tags.iterator();
        String NameIsUsed = null;
        while (it.hasNext()) {
            Tag temp = it.next();
            if(temp.getName().contains(tag.getName()) && !temp.getId().equals(tag.getId())){
                 NameIsUsed = "There is already a tag with the name " + tag.getName();
            }
        }

        if(tag.getName().length() == 0){
            model.addAttribute("allTags", tagService.findAll());
            model.addAttribute("errormessage", "Please give the tag a valid name");
            return "Tags/tags-manage";
        }

        if(NameIsUsed != null){
            model.addAttribute("allTags", tagService.findAll());
            model.addAttribute("errormessage", NameIsUsed);
            return "Tags/tags-manage";
        }

        if(result.hasErrors()){
            model.addAttribute("allTags", tagService.findAll());
            model.addAttribute("errormessage", "Oops, something went wrong!");

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
    public String viewMixturesList(final ModelMap model){
        model.addAttribute("allMixtures", mixtureService.findAll());
        return "/Mixtures/mixtures-list";
    }

    @PreAuthorize("hasAuthority('Stock - Modify - All')")
    @RequestMapping(value="/mixtures/{id}/delete",method = RequestMethod.GET)
    public String deleteMixture(@PathVariable Long id, final ModelMap model){
        mixtureService.deleteById(id);
        model.clear();
        String url = "redirect:/mixtures/";

        return url;
    }

    @PreAuthorize("hasAuthority('Stock - Modify - All') or hasAuthority('Stock - Aggregates + Bitumen Modify - Advanced')")
    @RequestMapping(value="/mixtures/{id}", method= RequestMethod.GET)
    public String viewEditMixture(@PathVariable Long id, final ModelMap model){
        model.addAttribute("mixture",mixtureService.findById(id).orElse(null));
        model.addAttribute("allCompositions", compositionService.findAll());
        model.addAttribute("allProducts", productService.findAll());
        return "/Mixtures/mixtures-manage";
    }

    @PreAuthorize("hasAuthority('Stock - Modify - All') or hasAuthority('Stock - Aggregates + Bitumen Modify - Advanced')")
    @RequestMapping(value={"/mixtures", "/mixtures/{id}"},
            method= RequestMethod.POST)
    public String addMixture(@Valid Mixture mixture, BindingResult result,
                             final ModelMap model){

        List<Mixture > mixtures = mixtureService.findAll();
        Iterator<Mixture> it = mixtures.iterator();
        String NameIsUsed = null;
        while (it.hasNext()) {
            Mixture temp = it.next();
            if(temp.getName().contains(mixture.getName()) && !temp.getId().equals(mixture.getId())){
                NameIsUsed = "There is already a mixture with the name " + mixture.getName();
            }
        }

        if(mixture.getName().length() == 0){
            model.addAttribute("allMixtures", mixtureService.findAll());
            model.addAttribute("errormessage", "Please enter a valid name");
            model.addAttribute("allCompositions", compositionService.findAll());

            return "Mixtures/mixtures-manage";
        }


        if(mixture.getCompositions().isEmpty()){
            model.addAttribute("allMixtures", mixtureService.findAll());
            model.addAttribute("errormessage", "Please select ingredients");
            model.addAttribute("allCompositions", compositionService.findAll());

            return "Mixtures/mixtures-manage";
        }

        if(NameIsUsed != null){
            model.addAttribute("allMixtures", tagService.findAll());
            model.addAttribute("errormessage", NameIsUsed);
            model.addAttribute("allCompositions", compositionService.findAll());

            return "/Mixtures/mixtures-manage";
        }

        if(result.hasErrors()){
            model.addAttribute("allMixtures", mixtureService.findAll());
            model.addAttribute("errormessage", "Oops, something went wrong!");
            model.addAttribute("allCompositions", compositionService.findAll());

            return "Mixtures/mixtures-manage";
        }
        mixtureService.save(mixture);
        model.addAttribute("allMixtures", mixtureService.findAll());
        return "/Mixtures/mixtures-list";
    }

    @PreAuthorize("hasAuthority('Stock - Modify - All') or hasAuthority('Stock - Aggregates + Bitumen Modify - Advanced')")
    @RequestMapping(value="/mixtures/put", method= RequestMethod.GET)
    public String viewCreateMixture(final ModelMap model){
        model.addAttribute("allMixtures", mixtureService.findAll());
        model.addAttribute("allProducts", productService.findAll());
        model.addAttribute("allCompositions", compositionService.findAll());
        model.addAttribute("mixture", new Mixture());

        return "/Mixtures/mixtures-manage";
    }

    @PreAuthorize("hasAuthority('Stock - Modify - All') or hasAuthority('Stock - Aggregates + Bitumen Modify - Advanced')")
    @RequestMapping(value="/compositions/{id}/delete",method = RequestMethod.GET)
    public String deleteIngredients(@PathVariable Long id, final ModelMap model){
        List<Mixture> mixtures = mixtureService.findAll();
        Composition compos = null;
        Optional<Composition> tempCompos = compositionService.findById(id);
        if(tempCompos.isPresent()){
            compos = tempCompos.get();
        }

        Iterator<Mixture> it = mixtures.iterator();
        while (it.hasNext()) {
            Mixture temp = it.next();
            if(temp.getCompositions().contains(compos) && temp.getId() != compos.getId()){
                List<Composition> list = temp.getCompositions();
                if(compos != null) {
                    list.remove(compos);
                    temp.setCompositions(list);
                }
            }
        }
        compositionService.deleteById(id);
        model.clear();
        String url = "redirect:/compositions/";

        return url;
    }

    @RequestMapping(value="/compositions", method= RequestMethod.GET)
    public String viewCompositionsList(final ModelMap model){
        model.addAttribute("allCompositions", compositionService.findAll());
        return "/Mixtures/compositions-list";
    }

    @PreAuthorize("hasAuthority('Stock - Modify - All') or hasAuthority('Stock - Aggregates + Bitumen Modify - Advanced')")
    @RequestMapping(value={"/compositions", "/compositions/{id}"},
            method= RequestMethod.POST)
    public String addComposition(@Valid Composition composition, BindingResult result,
                             final ModelMap model){
        if(composition.getAmount()<=0.0){
            System.out.println(composition.getAmount());
            model.addAttribute("errormessage", "Oops, the amount is invalid. Please choose a positive amount.");
            return "Mixtures/compositions-manage";
        }

        if(result.hasErrors()){
            model.addAttribute("allProducts", productService.findAll());
            model.addAttribute("errormessage", "Oops, something went wrong!");
            model.addAttribute("composition", new Composition());
            return "Mixtures/compositions-manage";
        }

        compositionService.save(composition);
        model.addAttribute("allCompositions", compositionService.findAll());
        return "/Mixtures/compositions-list";
    }

    @PreAuthorize("hasAuthority('Stock - Modify - All') or hasAuthority('Stock - Aggregates + Bitumen Modify - Advanced')")
    @RequestMapping(value="/compositions/{id}", method= RequestMethod.GET)
    public String viewEditComposition(@PathVariable Long id, final ModelMap model){
        model.addAttribute("allProducts", productService.findAll());
        model.addAttribute("allCompositions", compositionService.findAll());
        model.addAttribute("composition",compositionService.findById(id).orElse(null));
        return "/Mixtures/compositions-manage";
    }


    @PreAuthorize("hasAuthority('Stock - Modify - All') or hasAuthority('Stock - Aggregates + Bitumen Modify - Advanced')")
    @RequestMapping(value="/compositions/put", method= RequestMethod.GET)
    public String viewCreateComposition(final ModelMap model){
        model.addAttribute("allCompositions", compositionService.findAll());
        model.addAttribute("allProducts", productService.findAll());
        model.addAttribute("composition", new Composition());
        return "/Mixtures/compositions-manage";
    }


}
