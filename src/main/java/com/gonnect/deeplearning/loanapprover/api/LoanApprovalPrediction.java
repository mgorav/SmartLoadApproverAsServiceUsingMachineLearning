package com.gonnect.deeplearning.loanapprover.api;

import lombok.*;

import java.io.Serializable;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class LoanApprovalPrediction implements Serializable {

    private int labelIndex;
    private String label;
    private List<Double> classProbabilities;
    private Double intrestRate;

    public void addClassProbability(Double classProbability) {
        classProbabilities.add(classProbability);

    }
}
