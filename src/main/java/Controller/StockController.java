package Controller;


import be.uantwerpen.labplanner.common.model.stock.Product;
import be.uantwerpen.labplanner.common.service.stock.ProductService;
import be.uantwerpen.labplanner.common.service.stock.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
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

    @RequestMapping(value="/products", method= RequestMethod.GET)
    public String showProducts(final ModelMap model){
        model.addAttribute("allProducts",
                productService.findAll()); return "product-list";
    }
    @RequestMapping(value="/products/put", method= RequestMethod.GET)
    public String viewCreateProducts(final ModelMap model){
        model.addAttribute("allProducts", productService.findAll());
        model.addAttribute("product",new Product());
        return "product-manage";
    }
    @RequestMapping(value="/product/{id}", method= RequestMethod.GET)
    public String viewEditProduct(@PathVariable Long id, final ModelMap model){
        model.addAttribute("allProducts", productService.findAll());
        model.addAttribute("product",productService.findById(id).orElse(null)); return "product-manage";
    }
    @RequestMapping(value={"/product/", "/product/{id}"},
            method= RequestMethod.POST)
    public String addUser(@Valid Product user, BindingResult result,
                          final ModelMap model){
        if(result.hasErrors()){
            model.addAttribute("allTags", tagService.findAll());
            return "product-manage";
        }
        return "redirect:/product";
    }
    @RequestMapping(value="/product/{id}/delete")
    public String deleteProduct(@PathVariable Long id, final ModelMap
            model){ productService.deleteById(id);
        model.clear();
        return "redirect:/products";
    }
}