package dsl.generator
import dsl.greenhouse.MathNumber
import dsl.greenhouse.Plus
import dsl.greenhouse.Minus
import dsl.greenhouse.Mult
import dsl.greenhouse.Div

class MathGenerator {
	def dispatch String computeExpression(MathNumber exp) {
        exp.value.toString
    }

    def dispatch String computeExpression(Plus exp) {
        "(" + exp.left.computeExpression + "+" + exp.right.computeExpression + ")"
    }

    def dispatch String computeExpression(Minus exp) {
        "(" + exp.left.computeExpression + "-" + exp.right.computeExpression + ")"
    }

    def dispatch String computeExpression(Mult exp) {
        "(" + exp.left.computeExpression + "*" + exp.right.computeExpression + ")"
    }

    def dispatch String computeExpression(Div exp) {
        "(" + exp.left.computeExpression + "/" + exp.right.computeExpression + ")"
    }
}