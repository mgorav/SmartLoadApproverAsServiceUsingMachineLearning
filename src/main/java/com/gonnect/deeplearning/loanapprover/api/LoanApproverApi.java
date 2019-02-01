package com.gonnect.deeplearning.loanapprover.api;

import hex.genmodel.easy.EasyPredictModelWrapper;
import hex.genmodel.easy.RowData;
import hex.genmodel.easy.prediction.BinomialModelPrediction;
import hex.genmodel.easy.prediction.RegressionModelPrediction;
import io.swagger.annotations.Api;
import org.gradle.AtrociousLoanModel;
import org.gradle.LoanInterestRateModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.ArrayList;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/loan/approver")
@Api("Loan Approver")
public class LoanApproverApi {

    private EasyPredictModelWrapper badLoanModel;
    private EasyPredictModelWrapper interestRateModel;
    private AtrociousLoanModel rawBadLoanModel;
    private LoanInterestRateModel rawInterestRateModel;

    @GetMapping("/version")
    public String version() {

        return "0.0.1";
    }

    @PostMapping(path = "/predict",consumes = APPLICATION_JSON_VALUE , produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<LoanApprovalPrediction> predict(@RequestBody LoanApprovalDependentVariables dv) throws Exception {

        RowData rowData = new RowData();
        rowData.put("loan_amnt", dv.getLoan_amnt());
        rowData.put("term",dv.getTerm());
        rowData.put("int_rate",dv.getInt_rate());
        rowData.put("emp_length",dv.getEmp_length());
        rowData.put("home_ownership",dv.getHome_ownership());
        rowData.put("annual_inc",dv.getAnnual_inc());
        rowData.put("purpose",dv.getPurpose());
        rowData.put("addr_state",dv.getAddr_state());
        rowData.put("dti",dv.getDti());
        rowData.put("delinq_2yrs",dv.getDelinq_2yrs());
        rowData.put("revol_util",dv.getRevol_util());
        rowData.put("total_acc",dv.getTotal_acc());
        rowData.put("bad_loan",dv.getBad_loan());
        rowData.put("longest_credit_length",dv.getLongest_credit_length());
        rowData.put("verification_status",dv.getVerification_status());

        BinomialModelPrediction binomialModelPrediction = predictBadLoan(rowData);
        RegressionModelPrediction regressionModelPrediction = predictInterestRate(rowData);

        return new ResponseEntity<>(getLoadApprovalPrediction(binomialModelPrediction,regressionModelPrediction), OK);

    }

    @PostConstruct
    public void postConstruct() {
        AtrociousLoanModel rawBadLoanModel = new AtrociousLoanModel();
        badLoanModel = new EasyPredictModelWrapper(rawBadLoanModel);

        LoanInterestRateModel rawInterestRateModel = new LoanInterestRateModel();
        interestRateModel = new EasyPredictModelWrapper(rawInterestRateModel);
    }

    private BinomialModelPrediction predictBadLoan (RowData row) throws Exception {
        return badLoanModel.predictBinomial(row);
    }

    private RegressionModelPrediction predictInterestRate (RowData row) throws Exception {
        return interestRateModel.predictRegression(row);
    }

    private LoanApprovalPrediction getLoadApprovalPrediction(BinomialModelPrediction p, RegressionModelPrediction p2) {

        LoanApprovalPrediction prediction = new LoanApprovalPrediction();
        prediction.setLabel(p.label);
        prediction.setLabelIndex(p.labelIndex);

        prediction.setClassProbabilities(new ArrayList<>());
        for (int i = 0; i < p.classProbabilities.length; i++) {
            double d = p.classProbabilities[i];
            if (Double.isNaN(d)) {
                throw new RuntimeException("Probability is NaN");
            }
            else if (Double.isInfinite(d)) {
                throw new RuntimeException("Probability is infinite");
            }
            prediction.addClassProbability(d);

        }
        prediction.setIntrestRate(p2.value);



       return prediction;
    }



}
