package com.gonnect.deeplearning.loanapprover.api;

import lombok.*;

import java.io.Serializable;

@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class LoanApprovalDependentVariables implements Serializable {
    private String loan_amnt = "";
    private String term = "";
    private String int_rate = "";
    private String emp_length = "";
    private String home_ownership = "";
    private String annual_inc = "";
    private String purpose = "";
    private String addr_state = "";
    private String dti = "";
    private String delinq_2yrs = "";
    private String revol_util = "";
    private String total_acc = "";
    private String bad_loan = "";
    private String longest_credit_length = "";
    private String verification_status = "";


}
