package com.spark.drools.util

import scala.beans.BeanInfo
import scala.beans.BeanProperty

object Employee {

  case class EmployeeInput(empno: String=null, ename: String=null, job: String=null, mgr: String=null,
                           hiredate: String=null, sal: String=null, comm: String=null, deptno: String=null) {
    val empClass = this.getClass.getConstructors
    def createFromSeq(param: Seq[Any]) =
      empClass(0).newInstance(param map { _.asInstanceOf[AnyRef] }: _*).asInstanceOf[EmployeeInput]
  }

  @BeanInfo
  case class EmployeeResponse(@BeanProperty ruleName: String, @BeanProperty inputData: EmployeeInput, @BeanProperty rulesResponse: String)
}