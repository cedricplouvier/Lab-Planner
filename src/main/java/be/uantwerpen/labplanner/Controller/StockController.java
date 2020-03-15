package be.uantwerpen.labplanner.Controller;


import be.uantwerpen.labplanner.common.model.stock.Product;
import be.uantwerpen.labplanner.common.model.stock.Tag;
import be.uantwerpen.labplanner.common.service.stock.ProductService;
import be.uantwerpen.labplanner.common.service.stock.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

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
    @RequestMapping(value="/products", method= RequestMethod.GET)
    public String showProducts(final ModelMap model){
        model.addAttribute("allProducts",
                productService.findAll());
        return "/Stock/products-list";
    }
    @RequestMapping(value="/products/put", method= RequestMethod.GET)
    public String viewCreateProducts(final ModelMap model){
        model.addAttribute("allProducts", productService.findAll());
        model.addAttribute("product",new Product());
        return "/Stock/products-manage";
    }
    @RequestMapping(value="/products/{id}", method= RequestMethod.GET)
    public String viewEditProduct(@PathVariable Long id, final ModelMap model){
        model.addAttribute("allProducts", productService.findAll());
        model.addAttribute("allTags", tagService.findAll());
        model.addAttribute("product",productService.findById(id).orElse(null));
        return "/Stock/products-manage";
    }
    @RequestMapping(value={"/products/", "/products/{id}"},
            method= RequestMethod.POST)
    public String addProduct(@Valid Product product, BindingResult result,
                          final ModelMap model){
        if(result.hasErrors()){
            model.addAttribute("allTags", tagService.findAll());
            return "Stock/products-manage";
        }
        productService.save(product);
        return "redirect:/products";
    }
    @RequestMapping(value="/products/{id}/delete")
    public String deleteProduct(@PathVariable Long id, final ModelMap
            model){ productService.deleteById(id);
        model.clear();
        return "redirect:/products";
    }

    @RequestMapping(value="/tags", method = RequestMethod.GET)
    public String showProductTags(final ModelMap model){
        model.addAttribute("allProductTags",tagService.findAll());
        return "/Stock/tags-list";
    }

    @RequestMapping(value="/tags/{id}", method= RequestMethod.GET)
    public String viewEditTag(@PathVariable Long id, final ModelMap model){
        model.addAttribute("allTags", tagService.findAll());
        model.addAttribute("tag",tagService.findById(id).orElse(null));
        return "/Stock/tags-list";
    }
    @RequestMapping(value={"/tags", "/tags/{id}"},
            method= RequestMethod.POST)
        public String addTag(@Valid Tag tag, BindingResult result,
                             final ModelMap model){
        if(result.hasErrors()){
            model.addAttribute("allTags", tagService.findAll());
            return "Stock/tags-list";
        }
        tagService.save(tag);
        return "redirect:/products";
    }
}