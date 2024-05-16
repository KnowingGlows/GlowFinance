package com.knowingglows.glowfinance;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.auth.User;

import java.util.Objects;

public class expense_description extends AppCompatActivity {

    public Double previousOperand;
    public Character currentOperation;
    public StringBuilder AmountString;

    AppCompatTextView
            AmountDisplay,User_GlowCoins;
    AppCompatTextView
            user_profilename;

    FirebaseAuth user;
    AppCompatImageView
            userdp;

    AppCompatButton

            btn0, btn1,
            btn2, btn3, btn4, btn5, btn6,
            btn7, btn8, btn9, btn_decimal,
            btn_substract, btn_add, btn_divide,
            btn_multiply, btn_delete;
    AppCompatButton

            glowcoin_btn,

            addexpense,
            addincome_toolbar_btn, addexpense_toolbar_btn,
            bottom_navigation_home,
            bottom_navigation_transactions, bottom_navigation_addrecords,
            bottom_navigation_profile, bottom_navigation_report;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_description);

        Instantiate();
        Toolbar();
        BottomNavigationBarFunctionality();
        UserSetup();
        AmountInput();
        UserSetupGlowCoins();
        addexpense.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(expense_description.this, add_expense.class);
                // Put the final amount string as an extra in the Intent
                intent.putExtra("AMOUNT_STRING", AmountString.toString());
                // Start the new activity
                startActivity(intent);
            }
        });

        user = FirebaseAuth.getInstance();
        userdp = findViewById(R.id.user_profile);
        CircularProfilePic.loadCircularImage(this, userdp);
        GlowCoinsPageOpener.setButtonClickListener(expense_description.this, glowcoin_btn);
    }

    public void Instantiate() {

        glowcoin_btn = findViewById(R.id.glowcoin_btn);
        previousOperand = 0d;
        currentOperation = ' ';
        AmountString = new StringBuilder();
        AmountDisplay = findViewById(R.id.AmountDisplay);
        btn1 = findViewById(R.id.btn1);
        btn2 = findViewById(R.id.btn2);
        btn3 = findViewById(R.id.btn3);
        btn4 = findViewById(R.id.btn4);
        btn5 = findViewById(R.id.btn5);
        btn6 = findViewById(R.id.btn6);
        btn7 = findViewById(R.id.btn7);
        btn8 = findViewById(R.id.btn8);
        btn9 = findViewById(R.id.btn9);
        btn0 = findViewById(R.id.btn0);
        btn_add = findViewById(R.id.btn_add);
        btn_decimal = findViewById(R.id.btn_decimal);
        btn_divide = findViewById(R.id.btn_divide);
        btn_multiply = findViewById(R.id.btn_multiply);
        btn_substract = findViewById(R.id.btn_substract);
        btn_delete = findViewById(R.id.btn_delete);
        user_profilename = findViewById(R.id.user_username);
        addexpense = findViewById(R.id.add_expense_record);
        addexpense_toolbar_btn = findViewById(R.id.addexpense_toolbar_btn);
        addincome_toolbar_btn = findViewById(R.id.addincome_toolbar_btn);
        bottom_navigation_home = findViewById(R.id.bottom_navigation_home);
        bottom_navigation_transactions = findViewById(R.id.bottom_navigation_transactions);
        bottom_navigation_addrecords = findViewById(R.id.bottom_navigation_addrecords);
        bottom_navigation_profile = findViewById(R.id.bottom_navigation_profile);
        bottom_navigation_report = findViewById(R.id.bottom_navigation_report);
    }

    public void BottomNavigationBarFunctionality() {

        bottom_navigation_addrecords.setBackgroundTintList(ContextCompat.getColorStateList(expense_description.this, R.color.colourpalette_moderngreen));
        bottom_navigation_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bottom_navigation_home.setBackgroundTintList(ContextCompat.getColorStateList(expense_description.this, R.color.colourpalette_moderngreen));
                startActivity(new Intent(expense_description.this, home.class));
            }
        });

        bottom_navigation_transactions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottom_navigation_transactions.setBackgroundTintList(ContextCompat.getColorStateList(expense_description.this, R.color.colourpalette_moderngreen));
                startActivity(new Intent(expense_description.this, transactions.class));
            }
        });

        bottom_navigation_addrecords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottom_navigation_addrecords.setBackgroundTintList(ContextCompat.getColorStateList(expense_description.this, R.color.colourpalette_moderngreen));
                startActivity(new Intent(expense_description.this, income_description.class));
            }
        });

        bottom_navigation_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottom_navigation_profile.setBackgroundTintList(ContextCompat.getColorStateList(expense_description.this, R.color.colourpalette_moderngreen));
                startActivity(new Intent(expense_description.this, profile.class));
            }
        });

        bottom_navigation_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottom_navigation_report.setBackgroundTintList(ContextCompat.getColorStateList(expense_description.this, R.color.colourpalette_moderngreen));
                startActivity(new Intent(expense_description.this, report.class));
            }
        });
    }

    public void Toolbar() {
        addincome_toolbar_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(expense_description.this, income_description.class));
            }
        });

        addexpense_toolbar_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(expense_description.this, expense_description.class));
            }
        });
    }

    public void UpdateAmount() {
        AmountDisplay.setText(AmountString);
    }

    public void AppendAmount(@NonNull String digit) {
        if (digit.equals(".") && AmountString.indexOf(".") != -1) {
            // Decimal point already exists, ignore
            return;
        }
        if (currentOperation != ' ') {
            // Clear AmountString to start a new number
            AmountString.setLength(0);
            currentOperation = ' ';
        }
        AmountString.append(digit);
        UpdateAmount();
    }

    public void ArithmeticOperation(Character Operation)
    {
        if (AmountString.length() > 0) {
            double currentOperand = Double.parseDouble(AmountString.toString());
            switch (currentOperation) {
                case '+':
                    previousOperand += currentOperand;
                    break;
                case '-':
                    previousOperand -= currentOperand;
                    break;
                case '*':
                    previousOperand *= currentOperand;
                    break;
                case '/':
                    if (currentOperand != 0) {
                        previousOperand /= currentOperand;
                    } else {
                        Toast.makeText(this, "Cannot Divide By 0!", Toast.LENGTH_SHORT).show();
                    }
                    break;
                default:
                    previousOperand = currentOperand;
                    break;
            }
            currentOperation = Operation;
            AmountString.setLength(0);
            AmountString.append(previousOperand);
            UpdateAmount();
        }
    }

        public void UserSetup ()
        {
            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            String firebaseUser = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getDisplayName();
            user_profilename.setText(firebaseUser);
        }
        public void AmountInput()
        {
            btn1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppendAmount("1");
                }
            });

            btn2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppendAmount("2");
                }
            });

            btn3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppendAmount("3");
                }
            });

            btn4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppendAmount("4");
                }
            });

            btn5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppendAmount("5");
                }
            });

            btn6.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppendAmount("6");
                }
            });

            btn7.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppendAmount("7");
                }
            });
            btn8.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppendAmount("8");
                }
            });
            btn9.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppendAmount("9");
                }
            });
            btn0.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppendAmount("0");
                }
            });
            btn_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ArithmeticOperation('+');

                }
            });
            btn_substract.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ArithmeticOperation('-');
                }
            });
            btn_multiply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ArithmeticOperation('*');
                }
            });
            btn_divide.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ArithmeticOperation('/');
                }
            });
            btn_decimal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!AmountString.toString().contains(".")) {
                        AmountString.append(".");
                        UpdateAmount();
                    }
                }
            });

            btn_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (AmountString.length() > 0) {
                        AmountString.deleteCharAt(AmountString.length() - 1);
                        UpdateAmount();
                    }
                }
            });
        }

    public void UserSetupGlowCoins()
    {
        User_GlowCoins = findViewById(R.id.user_glowcoins_num);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        String firebaseUser = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getDisplayName();
        user_profilename.setText(firebaseUser);
        GlowCoins glowCoins = new GlowCoins();
        GlowCoins.getGlowCoins(firebaseAuth.getCurrentUser().getUid(), new GlowCoins.OnGlowCoinsLoadedListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onGlowCoinsLoaded(long glowCoins) {
                String GlowCoins = String.valueOf(glowCoins);
                User_GlowCoins.setText(GlowCoins);
            }

            @Override
            public void onError(String errorMessage) {

            }
        });
    }
    }