import h2o
h2o.init()

print("Import approved and rejected loan requests...")
loaded_loans_data = h2o.import_file(path ="data/loan.csv")
loaded_loans_data["bad_loan"] = loaded_loans_data["bad_loan"].asfactor()

rand  = loaded_loans_data.runif(seed = 1234567)
train = loaded_loans_data[rand <= 0.8]
valid = loaded_loans_data[rand > 0.8]

Y = "bad_loan"
X = ["loan_amnt", "longest_credit_length", "revol_util", "emp_length",
       "home_ownership", "annual_inc", "purpose", "addr_state", "dti",
       "delinq_2yrs", "total_acc", "verification_status", "term"]

from h2o.estimators.gbm import H2OGradientBoostingEstimator
loans_model = H2OGradientBoostingEstimator(score_each_iteration = True,
                                           ntrees = 100,
                                           max_depth = 5,
                                           learn_rate = 0.05,
                                           model_id = "BadLoanModel")
loans_model.train(x = X, y = Y, training_frame = train, validation_frame = valid)
print(loans_model)

# Download generated POJO for model
import os
if not os.path.exists("tmp"):
    os.makedirs("tmp")
loans_model.download_pojo(path ="tmp")

Y = "int_rate"
X = ["loan_amnt", "longest_credit_length", "revol_util", "emp_length",
       "home_ownership", "annual_inc", "purpose", "addr_state", "dti",
       "delinq_2yrs", "total_acc", "verification_status", "term"]

loans_model = H2OGradientBoostingEstimator(score_each_iteration = True,
                                           ntrees = 100,
                                           max_depth = 5,
                                           learn_rate = 0.05,
                                           model_id = "InterestRateModel")
loans_model.train(x = X, y = Y, training_frame = train, validation_frame = valid)
print(loans_model)

# Download generated POJO for model
loans_model.download_pojo(path ="tmp")
