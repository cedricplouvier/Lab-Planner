package be.uantwerpen.labplanner.Controller;


import be.uantwerpen.labplanner.Model.Composition;
import be.uantwerpen.labplanner.Model.Mixture;
import be.uantwerpen.labplanner.Model.OwnProduct;
import be.uantwerpen.labplanner.Model.OwnTag;
import be.uantwerpen.labplanner.Service.CompositionService;
import be.uantwerpen.labplanner.Service.MixtureService;
import be.uantwerpen.labplanner.Service.OwnProductService;
import be.uantwerpen.labplanner.Service.OwnTagService;
import be.uantwerpen.labplanner.common.model.stock.Unit;
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
    private OwnProductService productService;
    @Autowired
    private OwnTagService tagService;
    @Autowired
    private MixtureService mixtureService;
    @Autowired
    private CompositionService compositionService;

    //Populate
    @ModelAttribute("allProducts")
    public Iterable<OwnProduct> populateProducts(){
        return  this.productService.findAll();
    }

    public List<OwnProduct> getAggBitList(){
        //aggregates + bitumen
        List<OwnProduct> agg_bit = new ArrayList<>();
        //consumables + other
        List<OwnProduct> con_oth = new ArrayList<>();
        OwnTag aggregateTag = null;
        OwnTag bitumenTag = null;
        Optional<OwnTag> OptAggregateTag = tagService.findByName("Aggregates");
        Optional<OwnTag> optBitumenTag = tagService.findByName("Bitumen");
        if(OptAggregateTag.isPresent()){
            aggregateTag = OptAggregateTag.get();
        }
        if(optBitumenTag.isPresent()){
            bitumenTag = optBitumenTag.get();
        }


        List<OwnProduct> products = productService.findAll();
        Iterator<OwnProduct> it = products.iterator();
        while (it.hasNext()) {
            OwnProduct temp = it.next();
            if(temp.getTags().contains(aggregateTag) || temp.getTags().contains(bitumenTag)){
                agg_bit.add(temp);
            }
            else {
                con_oth.add(temp);
            }
        }
        return agg_bit;
    }

    public List<OwnProduct> getComOthList(){
        //aggregates + bitumen
        List<OwnProduct> agg_bit = new ArrayList<>();
        //consumables + other
        List<OwnProduct> con_oth = new ArrayList<>();
        OwnTag aggregateTag = null;
        OwnTag bitumenTag = null;
        Optional<OwnTag> OptAggregateTag = tagService.findByName("Aggregates");
        Optional<OwnTag> optBitumenTag = tagService.findByName("Bitumen");
        if(OptAggregateTag.isPresent()){
            aggregateTag = OptAggregateTag.get();
        }
        if(optBitumenTag.isPresent()){
            bitumenTag = optBitumenTag.get();
        }


        List<OwnProduct> products = productService.findAll();
        Iterator<OwnProduct> it = products.iterator();
        while (it.hasNext()) {
            OwnProduct temp = it.next();
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
        List<OwnProduct> agg_bit = getAggBitList();
        List<OwnProduct> con_oth = getComOthList();

        model.addAttribute("agg_bit", agg_bit);
        model.addAttribute("con_oth", con_oth);
        model.addAttribute("allMixtures", mixtureService.findAll());
        model.addAttribute("allProductTags", tagService.findAll());
        return "Stock/overview-stock";
    }



    @PreAuthorize("hasAuthority('Stock - Modify - All') or hasAuthority('Stock - Aggregates + Bitumen Modify - Advanced')")
    @RequestMapping(value="/products/put", method= RequestMethod.GET)
    public String viewCreateProducts(final ModelMap model){
        model.addAttribute("allProducts", productService.findAll());
        model.addAttribute("allTags", tagService.findAll());
        model.addAttribute("product",new OwnProduct("","",0.0, 0.0, 0.0, 0.0, null, "URL", "", null,null, LocalDateTime.now(), LocalDateTime.now(), null));
        model.addAttribute("units",Unit.values());
        return "Stock/products-manage";
    }

    @PreAuthorize("hasAuthority('Stock - Modify - All') or hasAuthority('Stock - Aggregates + Bitumen Modify - Advanced')")
    @RequestMapping(value="/products/{id}", method= RequestMethod.GET)
    public String viewEditProduct(@PathVariable Long id, final ModelMap model){
        model.addAttribute("allProducts", productService.findAll());
        model.addAttribute("allTags", tagService.findAll());
        model.addAttribute("product",productService.findById(id).orElse(null));
        model.addAttribute("units", Unit.values());
        return "Stock/products-manage";
    }

    @PreAuthorize("hasAuthority('Stock - Modify - All') or hasAuthority('Stock - Aggregates + Bitumen Modify - Advanced')")
    @RequestMapping(value={"/products/", "/products/{id}"},
            method= RequestMethod.POST)
    public String addProduct(@Valid OwnProduct ownProduct, BindingResult result,
                             final ModelMap model){
        Locale current = LocaleContextHolder.getLocale();

        List<OwnProduct> products = productService.findAll();
        Iterator<OwnProduct> it = products.iterator();
        String NameIsUsed = null;
        while (it.hasNext()) {
            OwnProduct temp = it.next();
            if(temp.getName().contains(ownProduct.getName()) && !temp.getId().equals(ownProduct.getId())){
                NameIsUsed = "There is already a product with the name " + ownProduct.getName();
            }
        }

        if(ownProduct.getName().length() == 0 ){
            model.addAttribute("allTags", tagService.findAll());
            model.addAttribute("product",ownProduct);
            model.addAttribute("errormessage", ResourceBundle.getBundle("messages",current).getString("valid.name"));
            model.addAttribute("units", Unit.values());

            return "Stock/products-manage";
        }

        if(ownProduct.getDescription().length() == 0 ){
            model.addAttribute("allTags", tagService.findAll());
            model.addAttribute("product",ownProduct);
            model.addAttribute("errormessage", ResourceBundle.getBundle("messages",current).getString("valid.description"));
            model.addAttribute("units", Unit.values());

            return "Stock/products-manage";
        }

        if(ownProduct.getProperties().length() == 0 ){
            model.addAttribute("allTags", tagService.findAll());
            model.addAttribute("product",ownProduct);
            model.addAttribute("errormessage", ResourceBundle.getBundle("messages",current).getString("valid.properties"));
            model.addAttribute("units", Unit.values());

            return "Stock/products-manage";
        }

        if(ownProduct.getStockLevel() < 0){
            model.addAttribute("allTags", tagService.findAll());
            model.addAttribute("product",ownProduct);
            model.addAttribute("errormessage", ResourceBundle.getBundle("messages",current).getString("valid.stock"));
            model.addAttribute("units", Unit.values());

            return "Stock/products-manage";
        }

        if(ownProduct.getLowStockLevel() < 0){
            model.addAttribute("allTags", tagService.findAll());
            model.addAttribute("product",ownProduct);
            model.addAttribute("errormessage", ResourceBundle.getBundle("messages",current).getString("valid.lowstock"));
            model.addAttribute("units", Unit.values());

            return "Stock/products-manage";
        }

        if(ownProduct.getReservedStockLevel() < 0){
            model.addAttribute("allTags", tagService.findAll());
            model.addAttribute("product",ownProduct);
            model.addAttribute("errormessage", ResourceBundle.getBundle("messages",current).getString("valid.reservedstock"));
            model.addAttribute("units", Unit.values());

            return "Stock/products-manage";
        }

        if(ownProduct.getTags().size() == 0){
            model.addAttribute("allTags", tagService.findAll());
            model.addAttribute("product",ownProduct);
            model.addAttribute("errormessage", ResourceBundle.getBundle("messages",current).getString("valid.tag"));
            model.addAttribute("units", Unit.values());

            return "Stock/products-manage";
        }

        if(NameIsUsed != null){
            model.addAttribute("allTags", tagService.findAll());
            model.addAttribute("product",ownProduct);
            model.addAttribute("errormessage", ResourceBundle.getBundle("messages",current).getString("valid.duplicate"));
            model.addAttribute("units", Unit.values());

            return "Stock/products-manage";
        }

        if(result.hasErrors()){
            model.addAttribute("allTags", tagService.findAll());
            model.addAttribute("product",ownProduct);
            model.addAttribute("errormessage", ResourceBundle.getBundle("messages",current).getString("valid.general"));
            model.addAttribute("units", Unit.values());

            return "Stock/products-manage";
        }
        productService.save(ownProduct);
        List<OwnProduct> agg_bit = getAggBitList();
        List<OwnProduct> con_oth = getComOthList();
        model.addAttribute("success", ResourceBundle.getBundle("messages",current).getString("save.success"));
        model.addAttribute("agg_bit", agg_bit);
        model.addAttribute("con_oth", con_oth);
        model.addAttribute("allMixtures", mixtureService.findAll());
        model.addAttribute("allProductTags", tagService.findAll());
        return "Stock/overview-stock";
    }

    @PreAuthorize("hasAuthority('Stock - Modify - All')")
    @RequestMapping(value="/products/{id}/delete",method = RequestMethod.GET)
    public String deleteProduct(@PathVariable Long id, final ModelMap model){
        Locale current = LocaleContextHolder.getLocale();
        Boolean inuse = false;
        OwnProduct prod = productService.findById(id).orElse(null);
        //check for all mixtures if this product is used as ingredient
        List<Composition> compositions = compositionService.findAll();
        Iterator<Composition> it = compositions.iterator();
        while (it.hasNext()) {
            Composition temp = it.next();
            if(temp.getProduct().equals(prod)){
                inuse = true;

            }
        }
        if(!inuse) {
            productService.deleteById(id);
            model.clear();
            List<OwnProduct> agg_bit = getAggBitList();
            List<OwnProduct> con_oth = getComOthList();
            model.addAttribute("success", ResourceBundle.getBundle("messages", current).getString("delete.success"));
            model.addAttribute("agg_bit", agg_bit);
            model.addAttribute("con_oth", con_oth);
            model.addAttribute("allMixtures", mixtureService.findAll());
            model.addAttribute("allProductTags", tagService.findAll());
            return "Stock/overview-stock";
        }
        else{
            List<OwnProduct> agg_bit = getAggBitList();
            List<OwnProduct> con_oth = getComOthList();
            model.addAttribute("error", ResourceBundle.getBundle("messages",current).getString("product.deleteError"));
            model.addAttribute("agg_bit", agg_bit);
            model.addAttribute("con_oth", con_oth);
            model.addAttribute("allMixtures", mixtureService.findAll());
            model.addAttribute("allProductTags", tagService.findAll());
            return "Stock/overview-stock";
        }

    }

    @RequestMapping(value ="/products/info/{id}", method= RequestMethod.GET)
    public String viewProductInfo(@PathVariable Long id, final ModelMap model){

        OwnProduct prod = productService.findById(id).orElse(null);

        List<Mixture> mixtures = new ArrayList<>();
        List<Mixture> returnList = new ArrayList<>();
        mixtures = mixtureService.findAll();
        Iterator<Mixture> it = mixtures.iterator();
        while (it.hasNext()) {
            Mixture mix = it.next();
            List<Composition> compositions = mix.getCompositions();
            Iterator<Composition> it2 = compositions.iterator();
            while (it2.hasNext()) {
                Composition comp = it2.next();
                if (comp.getProduct().equals(prod)) {
                    returnList.add(mix);
                }
            }

        }

        model.addAttribute("product",productService.findById(id).orElse(null));
        model.addAttribute("allTags", tagService.findAll());
        model.addAttribute("mixtures",returnList);
        return "Stock/products-info";
    }



    @PreAuthorize("hasAuthority('Stock - Modify - All')")
    @RequestMapping(value="/tags/{id}/delete",method = RequestMethod.GET)
    public String deleteTag(@PathVariable Long id, final ModelMap model){
        Locale current = LocaleContextHolder.getLocale();
        List<OwnProduct> products = productService.findAll();
        List<Mixture> mixtures = mixtureService.findAll();
        OwnTag tag = null;
        Optional<OwnTag> tempTag = tagService.findById(id);
        boolean isUsed = false;
        if(tempTag.isPresent()){
            tag = tempTag.get();
        }

        Iterator<OwnProduct> it = products.iterator();
        while (it.hasNext()) {
            OwnProduct temp = it.next();
            if(temp.getTags().contains(tag)){
                isUsed = true;
            }
        }
        Iterator<Mixture> it2 = mixtures.iterator();
        while (it2.hasNext()) {
            Mixture temp = it2.next();
            if(temp.getTags().contains(tag)){
                isUsed = true;
            }
        }
        if (isUsed){
            List<OwnProduct> agg_bit = getAggBitList();
            List<OwnProduct> con_oth = getComOthList();
            model.addAttribute("agg_bit", agg_bit);
            model.addAttribute("con_oth", con_oth);
            model.addAttribute("allMixtures", mixtureService.findAll());
            model.addAttribute("allProductTags", tagService.findAll());
            model.addAttribute("error", ResourceBundle.getBundle("messages",current).getString("tag.deleteError"));
            return "Stock/overview-stock";
        }

        tagService.deleteById(id);
        List<OwnProduct> agg_bit = getAggBitList();
        List<OwnProduct> con_oth = getComOthList();
        model.addAttribute("agg_bit", agg_bit);
        model.addAttribute("con_oth", con_oth);
        model.addAttribute("allMixtures", mixtureService.findAll());
        model.addAttribute("allProductTags", tagService.findAll());
        model.addAttribute("success", ResourceBundle.getBundle("messages",current).getString("delete.success"));
        return "Stock/overview-stock";
    }



    @PreAuthorize("hasAuthority('Stock - Modify - All')")
    @RequestMapping(value="/tags/{id}", method= RequestMethod.GET)
    public String viewEditTag(@PathVariable Long id, final ModelMap model){
        model.addAttribute("allTags", tagService.findAll());
        model.addAttribute("tag",tagService.findById(id).orElse(null));
        return "Tags/tags-manage";
    }

    @PreAuthorize("hasAuthority('Stock - Modify - All')")
    @RequestMapping(value={"/tags", "/tags/{id}"},
            method= RequestMethod.POST)
        public String addTag(@Valid OwnTag tag, BindingResult result,
                             final ModelMap model){
        Locale current = LocaleContextHolder.getLocale();

        List<OwnTag> tags = tagService.findAll();
        Iterator<OwnTag> it = tags.iterator();
        String NameIsUsed = null;
        while (it.hasNext()) {
            OwnTag temp = it.next();
            if(temp.getName().equals(tag.getName()) && !temp.getId().equals(tag.getId())){
                 NameIsUsed = "There is already a tag with the name " + tag.getName();
            }
        }

        if(tag.getName().length() == 0){
            model.addAttribute("tag", new OwnTag());
            model.addAttribute("allTags", tagService.findAll());
            model.addAttribute("errormessage", ResourceBundle.getBundle("messages",current).getString("valid.name"));
            return "Tags/tags-manage";
        }

        if(NameIsUsed != null){
            model.addAttribute("tag", new OwnTag());
            model.addAttribute("allTags", tagService.findAll());
            model.addAttribute("errormessage", ResourceBundle.getBundle("messages",current).getString("valid.duplicate"));
            return "Tags/tags-manage";
        }

        if(result.hasErrors()){
            model.addAttribute("tag", new OwnTag());
            model.addAttribute("allTags", tagService.findAll());
            model.addAttribute("errormessage", ResourceBundle.getBundle("messages",current).getString("valid.general"));

            return "Tags/tags-manage";
        }
        tagService.save(tag);
        List<OwnProduct> agg_bit = getAggBitList();
        List<OwnProduct> con_oth = getComOthList();
        model.addAttribute("success", ResourceBundle.getBundle("messages",current).getString("save.success"));
        model.addAttribute("agg_bit", agg_bit);
        model.addAttribute("con_oth", con_oth);
        model.addAttribute("allMixtures", mixtureService.findAll());
        model.addAttribute("allProductTags", tagService.findAll());
        return "Stock/overview-stock";
    }

    @PreAuthorize("hasAuthority('Stock - Modify - All')")
    @RequestMapping(value="/tags/put", method= RequestMethod.GET)
    public String viewCreateTags(final ModelMap model){
        model.addAttribute("allTags", tagService.findAll());
        model.addAttribute("tag", new OwnTag(""));
        return "Tags/tags-manage";
    }



    @PreAuthorize("hasAuthority('Stock - Modify - All')")
    @RequestMapping(value="/mixtures/{id}/delete",method = RequestMethod.GET)
    public String deleteMixture(@PathVariable Long id, final ModelMap model){
        Locale current = LocaleContextHolder.getLocale();
        /*for(Composition comp: mixtureService.findById(id).orElse(null).getCompositions()){
            compositionService.delete(comp);
        }
        mixtureService.deleteById(id);
        model.clear();
        List<OwnProduct> agg_bit = getAggBitList();
        List<OwnProduct> con_oth = getComOthList();
        model.addAttribute("success", ResourceBundle.getBundle("messages",current).getString("delete.success"));
        model.addAttribute("agg_bit", agg_bit);
        model.addAttribute("con_oth", con_oth);
        model.addAttribute("allMixtures", mixtureService.findAll());
        model.addAttribute("allProductTags", tagService.findAll());
        return "Stock/overview-stock";*/
        List<OwnProduct> agg_bit = getAggBitList();
        List<OwnProduct> con_oth = getComOthList();
        model.addAttribute("error", ResourceBundle.getBundle("messages",current).getString("mixture.deleteError"));
        model.addAttribute("agg_bit", agg_bit);
        model.addAttribute("con_oth", con_oth);
        model.addAttribute("allMixtures", mixtureService.findAll());
        model.addAttribute("allProductTags", tagService.findAll());
        return "Stock/overview-stock";
    }

    @PreAuthorize("hasAuthority('Stock - Modify - All') or hasAuthority('Stock - Aggregates + Bitumen Modify - Advanced')")
    @RequestMapping(value="/mixtures/{id}", method= RequestMethod.GET)
    public String viewEditMixture(@PathVariable Long id, final ModelMap model){
        model.addAttribute("mixture",mixtureService.findById(id).orElse(null));
        model.addAttribute("allProducts", productService.findAll());
        model.addAttribute("allTags", tagService.findAll());
        model.addAttribute("allMixtures", mixtureService.findAll());
        model.addAttribute("allCompositions", compositionService.findAll());
        model.addAttribute("composition", new Composition());

        return "Mixtures/mixtures-manage";

    }

    @PreAuthorize("hasAuthority('Stock - Modify - All') or hasAuthority('Stock - Aggregates + Bitumen Modify - Advanced')")
    @RequestMapping(value={"/mixtures", "/mixtures/{id}"},
            method= RequestMethod.POST)
    public String addMixture(@Valid Mixture mixture, BindingResult result,
                             final ModelMap model){
        Locale current = LocaleContextHolder.getLocale();

        List<Mixture > mixtures = mixtureService.findAll();
        Iterator<Mixture> it = mixtures.iterator();
        String NameIsUsed = null;
        while (it.hasNext()) {
            Mixture temp = it.next();
            if(temp.getName().contains(mixture.getName()) && !temp.getId().equals(mixture.getId())){
                NameIsUsed = "There is already a mixture with the name " + mixture.getName();
            }
        }

        //calculate total amount, it needs to be 100
        Iterator<Composition> it2 = mixture.getCompositions().iterator();
        Double totalAmount = 0.0;
        while (it2.hasNext()) {
            Composition temp = it2.next();
            totalAmount = totalAmount + temp.getAmount();

        }

        //remove mixtures with amount = from list
        Iterator<Composition> it3 = mixture.getCompositions().iterator();
        while (it3.hasNext()) {
            Composition temp = it3.next();
            if (temp.getAmount() == 0) {
                mixture.getCompositions().remove(temp);
            }

        }

        if(mixture.getName().length() == 0){
            model.addAttribute("allMixtures", mixtureService.findAll());
            model.addAttribute("errormessage", ResourceBundle.getBundle("messages",current).getString("valid.name"));
            model.addAttribute("allProducts", productService.findAll());
            model.addAttribute("allTags", tagService.findAll());
            model.addAttribute("composition", new Composition());
            model.addAttribute("allCompositions", compositionService.findAll());

            return "Mixtures/mixtures-manage";
        }

        if(mixture.getDescription().length() == 0){
            model.addAttribute("allMixtures", mixtureService.findAll());
            model.addAttribute("errormessage", ResourceBundle.getBundle("messages",current).getString("valid.description"));
            model.addAttribute("allProducts", productService.findAll());
            model.addAttribute("allTags", tagService.findAll());
            model.addAttribute("composition", new Composition());
            model.addAttribute("allCompositions", compositionService.findAll());

            return "Mixtures/mixtures-manage";
        }

        if(mixture.getTags().size() == 0 || mixture.getTags() == null){
            model.addAttribute("allMixtures", mixtureService.findAll());
            model.addAttribute("errormessage", ResourceBundle.getBundle("messages",current).getString("valid.tag"));
            model.addAttribute("allProducts", productService.findAll());
            model.addAttribute("allTags", tagService.findAll());
            model.addAttribute("composition", new Composition());
            model.addAttribute("allCompositions", compositionService.findAll());

            return "Mixtures/mixtures-manage";
        }


        if(totalAmount != 100.0){
            model.addAttribute("allMixtures", mixtureService.findAll());
            model.addAttribute("errormessage", ResourceBundle.getBundle("messages",current).getString("valid.totalAmount"));
            model.addAttribute("amount", totalAmount);

            model.addAttribute("allProducts", productService.findAll());
            model.addAttribute("allTags", tagService.findAll());
            model.addAttribute("composition", new Composition());
            model.addAttribute("allCompositions", compositionService.findAll());

            return "Mixtures/mixtures-manage";
        }


        if(mixture.getCompositions().isEmpty()){
            model.addAttribute("allMixtures", mixtureService.findAll());
            model.addAttribute("errormessage", ResourceBundle.getBundle("messages",current).getString("valid.ingredients"));
            model.addAttribute("allProducts", productService.findAll());
            model.addAttribute("composition", new Composition());
            model.addAttribute("allTags", tagService.findAll());
            model.addAttribute("allCompositions", compositionService.findAll());

            return "Mixtures/mixtures-manage";
        }



        if(NameIsUsed != null){
            model.addAttribute("allMixtures", tagService.findAll());
            model.addAttribute("errormessage", ResourceBundle.getBundle("messages",current).getString("valid.duplicate"));
            model.addAttribute("allProducts", productService.findAll());
            model.addAttribute("allTags", tagService.findAll());
            model.addAttribute("composition", new Composition());
            model.addAttribute("allCompositions", compositionService.findAll());

            return "Mixtures/mixtures-manage";
        }

        if(result.hasErrors()){
            model.addAttribute("allMixtures", mixtureService.findAll());
            model.addAttribute("errormessage", ResourceBundle.getBundle("messages",current).getString("valid.general"));
            model.addAttribute("allProducts", productService.findAll());
            model.addAttribute("allTags", tagService.findAll());
            model.addAttribute("composition", new Composition());
            model.addAttribute("allCompositions", compositionService.findAll());

            return "Mixtures/mixtures-manage";
        }

        for (Composition comp: mixture.getCompositions()){
            compositionService.save(comp);
        }

        mixtureService.save(mixture);
        List<OwnProduct> agg_bit = getAggBitList();
        List<OwnProduct> con_oth = getComOthList();
        model.addAttribute("success", ResourceBundle.getBundle("messages",current).getString("save.success"));
        model.addAttribute("agg_bit", agg_bit);
        model.addAttribute("con_oth", con_oth);
        model.addAttribute("allMixtures", mixtureService.findAll());
        model.addAttribute("allProductTags", tagService.findAll());
        return "Stock/overview-stock";
    }

    @PreAuthorize("hasAuthority('Stock - Modify - All') or hasAuthority('Stock - Aggregates + Bitumen Modify - Advanced')")
    @RequestMapping(value="/mixtures/put", method= RequestMethod.GET)
    public String viewCreateMixture(final ModelMap model){

        model.addAttribute("allMixtures", mixtureService.findAll());
        model.addAttribute("allProducts", productService.findAll());
        model.addAttribute("allCompositions", compositionService.findAll());
        model.addAttribute("mixture", new Mixture());
        model.addAttribute("allTags", tagService.findAll());
        model.addAttribute("composition", new Composition());

        return "Mixtures/mixtures-manage";
    }




    @RequestMapping(value ="/mixtures/info/{id}", method= RequestMethod.GET)
    public String viewMixtureInfo(@PathVariable Long id, final ModelMap model){
        model.addAttribute("mixture",mixtureService.findById(id).orElse(null));
        model.addAttribute("allProducts", productService.findAll());
        return "Mixtures/mixtures-info";
    }






}
