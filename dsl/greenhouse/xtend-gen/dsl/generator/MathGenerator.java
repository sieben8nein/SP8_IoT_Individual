package dsl.generator;

import dsl.greenhouse.Div;
import dsl.greenhouse.Expression;
import dsl.greenhouse.MathNumber;
import dsl.greenhouse.Minus;
import dsl.greenhouse.Mult;
import dsl.greenhouse.Plus;
import java.util.Arrays;

@SuppressWarnings("all")
public class MathGenerator {
  protected String _computeExpression(final MathNumber exp) {
    return Integer.valueOf(exp.getValue()).toString();
  }
  
  protected String _computeExpression(final Plus exp) {
    String _computeExpression = this.computeExpression(exp.getLeft());
    String _plus = ("(" + _computeExpression);
    String _plus_1 = (_plus + "+");
    String _computeExpression_1 = this.computeExpression(exp.getRight());
    String _plus_2 = (_plus_1 + _computeExpression_1);
    return (_plus_2 + ")");
  }
  
  protected String _computeExpression(final Minus exp) {
    String _computeExpression = this.computeExpression(exp.getLeft());
    String _plus = ("(" + _computeExpression);
    String _plus_1 = (_plus + "-");
    String _computeExpression_1 = this.computeExpression(exp.getRight());
    String _plus_2 = (_plus_1 + _computeExpression_1);
    return (_plus_2 + ")");
  }
  
  protected String _computeExpression(final Mult exp) {
    String _computeExpression = this.computeExpression(exp.getLeft());
    String _plus = ("(" + _computeExpression);
    String _plus_1 = (_plus + "*");
    String _computeExpression_1 = this.computeExpression(exp.getRight());
    String _plus_2 = (_plus_1 + _computeExpression_1);
    return (_plus_2 + ")");
  }
  
  protected String _computeExpression(final Div exp) {
    String _computeExpression = this.computeExpression(exp.getLeft());
    String _plus = ("(" + _computeExpression);
    String _plus_1 = (_plus + "/");
    String _computeExpression_1 = this.computeExpression(exp.getRight());
    String _plus_2 = (_plus_1 + _computeExpression_1);
    return (_plus_2 + ")");
  }
  
  public String computeExpression(final Expression exp) {
    if (exp instanceof Div) {
      return _computeExpression((Div)exp);
    } else if (exp instanceof MathNumber) {
      return _computeExpression((MathNumber)exp);
    } else if (exp instanceof Minus) {
      return _computeExpression((Minus)exp);
    } else if (exp instanceof Mult) {
      return _computeExpression((Mult)exp);
    } else if (exp instanceof Plus) {
      return _computeExpression((Plus)exp);
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(exp).toString());
    }
  }
}
