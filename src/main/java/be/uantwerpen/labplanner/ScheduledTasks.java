package be.uantwerpen.labplanner;


import be.uantwerpen.labplanner.Controller.StepController;
import be.uantwerpen.labplanner.Model.*;
import be.uantwerpen.labplanner.Service.ExperimentService;
import be.uantwerpen.labplanner.Service.OwnProductService;
import be.uantwerpen.labplanner.Service.StepService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Component
public class ScheduledTasks {

    @Autowired
    private ExperimentService experimentService;

    @Autowired
    private StepService stepService;

    @Autowired
    private OwnProductService productService;

    private Logger logger = LoggerFactory.getLogger(ScheduledTasks.class);


    @Scheduled(cron = "0 59 23 * * ?")  //each day at 23:59
    public void adjustReservedstockExperiments() {

        String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());


        //iterate over all experiments
        for (Experiment exp : experimentService.findAll()) {
            //Get the data of the end experiment
            String endDate = exp.getEndDate();
            if(endDate.equals(today)){
                for(PieceOfMixture pom : exp.getPiecesOfMixture()){
                    Mixture mix = pom.getMixture();
                    List<Composition> compositions = mix.getCompositions();
                    for(Composition comp : compositions){
                        OwnProduct product = comp.getProduct();
                        double reservedStocklevel = product.getReservedStockLevel();
                        reservedStocklevel -= comp.getAmount()*pom.getMixtureAmount()/100;
                        product.setReservedStockLevel(reservedStocklevel);
                        productService.save(product);
                    }
                }
            }
        }

        logger.info("Reserved stock levels are adjusted -- EXPERIMENTS");

    }

    @Scheduled(cron = "0 59 23 * * ?")  //each day at 23:59
    public void adjustReservedstockIndividualSteps() {

        String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());


        //iterate over all steps
        for (Step step : stepService.findAll()) {
            //Get the data of the end experiment
            String endDate = step.getEnd();
            if(endDate.equals(today)) {
                if (step.getMixture() != null && step.getAmount() != 0.0) {
                    Mixture mix = step.getMixture();
                    List<Composition> compositions = mix.getCompositions();
                    for (Composition comp : compositions) {
                        OwnProduct product = comp.getProduct();
                        double reservedStocklevel = product.getReservedStockLevel();
                        reservedStocklevel -= comp.getAmount() * step.getAmount() / 100;
                        product.setReservedStockLevel(reservedStocklevel);
                        productService.save(product);
                    }
                }
            }
        }
        logger.info("Reserved stock levels are adjusted -- STEP");



    }
}

