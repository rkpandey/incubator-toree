/*
 * Copyright 2014 IBM Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ibm.spark.kernel.protocol.v5.content

import com.ibm.spark.kernel.protocol.v5._
import org.scalatest.{FunSpec, Matchers}
import play.api.data.validation.ValidationError
import play.api.libs.json._

class ExecuteReplySpec extends FunSpec with Matchers {
  val executeReplyJson: JsValue = Json.parse("""
  {
    "status": "<STRING>",
    "execution_count": 999
  }
  """)

  val executeReply: ExecuteReply = ExecuteReply(
    "<STRING>", 999, None, None, None, None, None
  )

  describe("ExecuteReply") {
    describe("implicit conversions") {
      it("should implicitly convert from valid json to a executeReply instance") {
        // This is the least safe way to convert as an error is thrown if it fails
        executeReplyJson.as[ExecuteReply] should be (executeReply)
      }

      it("should also work with asOpt") {
        // This is safer, but we lose the error information as it returns
        // None if the conversion fails
        val newExecuteReply = executeReplyJson.asOpt[ExecuteReply]

        newExecuteReply.get should be (executeReply)
      }

      it("should also work with validate") {
        // This is the safest as it collects all error information (not just first error) and reports it
        val executeReplyResults = executeReplyJson.validate[ExecuteReply]

        executeReplyResults.fold(
          (invalid: Seq[(JsPath, Seq[ValidationError])]) => println("Failed!"),
          (valid: ExecuteReply) => valid
        ) should be (executeReply)
      }

      it("should implicitly convert from a executeReply instance to valid json") {
        Json.toJson(executeReply) should be (executeReplyJson)
      }
    }
  }
}

