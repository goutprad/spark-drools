package com.spark.drools.service

import org.kie.api.runtime.rule.RuleContext
import com.spark.drools.util.Employee.EmployeeInput
import org.kie.api.runtime.KieSession
import com.spark.drools.util.Employee.EmployeeResponse

object DroolsFunctions {
  def insertResponse(ruleContext: RuleContext, input: EmployeeInput, action: String): Unit = {
    val ksession = ruleContext.getKieRuntime.asInstanceOf[KieSession]
    val ruleName = ruleContext.getRule.getName
    ksession.insert(EmployeeResponse(ruleName, input, action))
  }
}