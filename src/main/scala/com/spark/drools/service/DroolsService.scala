package com.spark.drools.service

import org.kie.internal.io.ResourceFactory
import org.kie.internal.builder.KnowledgeBuilderFactory
import org.kie.api.io.ResourceType
import org.drools.core.impl.KnowledgeBaseFactory
import org.drools.core.impl.InternalKnowledgeBase
import org.apache.spark.sql.Row
import org.kie.api.runtime.KieSession
import scala.collection.JavaConversions.collectionAsScalaIterable
import com.spark.drools.util.Employee.EmployeeInput
import com.spark.drools.util.Employee.EmployeeResponse

object DroolsService {
  /**
   * loadDrlFile - to load drl rule file
   *
   * @param drlFilepath
   *
   * @return InternalKnowledgeBase
   */
  def loadDrlFile(drlFilepath: String): InternalKnowledgeBase = {
    val resource = ResourceFactory.newFileResource(drlFilepath)
    val kBuilder = KnowledgeBuilderFactory.newKnowledgeBuilder()
    kBuilder.add(resource, ResourceType.DRL)

    if (kBuilder.hasErrors()) {
      throw new RuntimeException(kBuilder.getErrors.toString())
    }
    val kBase = KnowledgeBaseFactory.newKnowledgeBase()
    kBase.addPackages(kBuilder.getKnowledgePackages)
    kBase
  }

  /**
   * fireAllRules - to fire all rules
   *
   * @param kBase
   * @param inputData
   *
   * @return
   */
  def fireAllRules(kBase: InternalKnowledgeBase, inputData: Row): EmployeeResponse = {
    val input = EmployeeInput().createFromSeq(inputData.toSeq)
    val kSession = kBase.newKieSession()
    kSession.insert(input)
    kSession.fireAllRules()

    val empResponse = getResults(kSession, "EmployeeResponse") match {
      case Some(x) => x.asInstanceOf[EmployeeResponse]
      case None    => null
    }
    kSession.dispose()
    empResponse
  }

  /**
   * getResults - to get result after rules get executed
   *
   * @param kses: KieSession
   * @param className: String
   *
   * @return Option[Any]
   */
  def getResults(kses: KieSession, className: String): Option[Any] = {
    val itkses = kses.getObjects()
      .filter(obj => obj.getClass().getSimpleName.equals(className))
    if (itkses.size > 0) {
      Some(itkses.toList.head)
    } else {
      None
    }
  }
}