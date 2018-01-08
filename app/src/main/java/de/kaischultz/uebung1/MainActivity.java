package de.kaischultz.uebung1;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

import org.apache.commons.lang3.StringUtils;

public class MainActivity extends AppCompatActivity {

    private EditText calcout;
    private TextView resultText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        calcout =  findViewById(R.id.CalcOut);
        resultText = findViewById(R.id.result);


        Button ce = findViewById(R.id.del);

        ce.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                calcout.setText("");
                resultText.setText("");
                return false;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onRestart();
        if (calcout.getText().length() != 0) {
            calculate();
        }
    }

    private void calculate() {
        try {
            String expr = calcout.getText().toString();
            Expression e = new ExpressionBuilder(expr).build();
            double result = e.evaluate();
            resultText.setText(Double.toString(result));
        } catch (Exception e) {
            e.getMessage();
        }
    }


    public void btnOnClick(View view) {
        Button b = (Button) view;
        Editable term = calcout.getText();
//        Log.d("preventDoubleCalcSym", String.valueOf(preventDoubleCalcSym(b.getText())));
//        Log.d("Calcout", calcout.getText().toString());
        if (term.length() == 0) {
//            Log.i("btnOnClick", "if");
            if (StringUtils.isNumeric(b.getText()) || b.getText().equals("+") || b.getText().equals("-")) {
                calcout.setText(String.format("%s" + b.getText().toString(), term.toString()));
                calculate();
            }
        } else if (preventDoubleCalcSym(b.getText()) == 1) {
            calcout.setText(term.replace(term.length() - 1, term.length(), b.getText()));
            calculate();
        } else if (preventDoubleCalcSym(b.getText()) == 2) {
            calcout.setText(term.replace(term.length() - 2, term.length(), b.getText()));
            calculate();
        } else {
            calcout.append(b.getText());
            calculate();
        }
    }

    public void btnOnClickReturn(View view) {
        Editable term = calcout.getText();
        if (term.length() > 0) {
            calcout.setText(term.replace(term.length() - 1, term.length(), ""));
        }
    }

    private int preventDoubleCalcSym(CharSequence btnText) {
        Editable term = calcout.getText();

        CharSequence last = term.subSequence(term.length() - 1, term.length());
        CharSequence lastTwo = null;
        if (term.length() >= 2) {
            lastTwo = term.subSequence(term.length() - 2, term.length());
        }

        if (last.toString().equals("-") || last.toString().equals("+") || last.toString().equals("*") || last.toString().equals("/")) {
            if (lastTwo != null) {
                if ((lastTwo.toString().equals("*-") || lastTwo.toString().equals("/-")) && !StringUtils.isNumeric(btnText)) {
                    return 2;
                }
            }
            if (((last.toString().equals("*") || last.toString().equals("/")) && btnText.toString().equals("-"))) {
                return 0;
            }
            if (StringUtils.isNumeric(btnText)) {
                return 0;
            }
            Log.i("Replace", "YES!!!!!!");
            return 1;
        } else {

            return 0;
        }
    }

}
