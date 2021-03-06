package in.org.celesta2k18.activities;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.widget.EditText;
import android.util.TypedValue;
import android.view.View;
import com.transitionseverywhere.ChangeBounds;
import com.transitionseverywhere.Transition;
import com.transitionseverywhere.TransitionManager;
import com.transitionseverywhere.TransitionSet;
import java.util.List;
import java.util.Objects;
import butterknife.BindViews;
import butterknife.ButterKnife;
import in.org.celesta2k18.R;
import android.widget.Toast;

public class SignUpFragment extends AuthFragment{
    @BindViews(value = {R.id.email_input_edit,
            R.id.password_input_edit,
            R.id.confirm_password_edit})
    protected List<TextInputEditText> views;
    Bundle bundle;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        EditText password1 = (EditText)view.findViewById(R.id.password_input_edit);
        EditText password2 = (EditText)view.findViewById(R.id.confirm_password_edit);
        EditText emailHolder = (EditText)view.findViewById(R.id.email_input_edit);
        super.onViewCreated(view, savedInstanceState);
        view.setBackgroundColor(ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.color_sign_up));
        caption.setText(getString(R.string.sign_up_label));
        for (TextInputEditText editText : views) {
            if (editText.getId() == R.id.password_input_edit) {
                final TextInputLayout inputLayout = ButterKnife.findById(view, R.id.password_input);
                final TextInputLayout confirmLayout = ButterKnife.findById(view, R.id.confirm_password);
                Typeface boldTypeface = Typeface.defaultFromStyle(Typeface.BOLD);
                inputLayout.setTypeface(boldTypeface);
                confirmLayout.setTypeface(boldTypeface);
                editText.addTextChangedListener(new TextWatcherAdapter() {
                    @Override
                    public void afterTextChanged(Editable editable) {
                        inputLayout.setPasswordVisibilityToggleEnabled(editable.length() > 0);
                    }
                });
            }
            editText.setOnFocusChangeListener((temp, hasFocus) -> {
                if (!hasFocus) {
                    boolean isEnabled = editText.getText().length() > 0;
                    editText.setSelected(isEnabled);
                }
            });
        }
        Toast  errorPassToast = android.widget.Toast.makeText(getContext(), getResources().getString(R.string.PasswordMismatch), Toast.LENGTH_LONG);
        Toast  errorEmptyToast = android.widget.Toast.makeText(getContext(), getResources().getString(R.string.EmptyFields), Toast.LENGTH_LONG);
        caption.setOnClickListener(v -> {
            String pass1 = password1.getText().toString();
            String pass2 = password2.getText().toString();
            String email = emailHolder.getText().toString();
            if(pass1.equals("") && pass2.equals("") && email.equals(""))
            {

            }
            else if(pass1.equals("") || pass2.equals("") || email.equals(""))
            {
                errorEmptyToast.show();
            }
            else if(pass1.equals(pass2))
            {
                android.content.Intent intent = new android.content.Intent(getContext(), RegisterActivity.class);
                bundle = new Bundle();
                bundle.putString("email",email);
                bundle.putString("password",pass1);
                intent.putExtras(bundle);
                startActivity(intent);
            }
            else if(!pass1.equals(pass2) && !pass2.equals(""))
            {
                errorPassToast.show();
            }
        });
        caption.setVerticalText(true);
        foldStuff();
        caption.setTranslationX(getTextPadding());
    }

    @Override
    public int authLayout() {
        return R.layout.sign_up_fragment;
    }

    @Override
    public void clearFocus() {
        for(View view:views) view.clearFocus();
    }

    @Override
    public void fold() {
        lock=false;
        Rotate transition = new Rotate();
        transition.setEndAngle(-90f);
        transition.addTarget(caption);
        TransitionSet set=new TransitionSet();
        set.setDuration(getResources().getInteger(R.integer.duration));
        ChangeBounds changeBounds=new ChangeBounds();
        set.addTransition(changeBounds);
        set.addTransition(transition);
        TextSizeTransition sizeTransition=new TextSizeTransition();
        sizeTransition.addTarget(caption);
        set.addTransition(sizeTransition);
        set.setOrdering(TransitionSet.ORDERING_TOGETHER);
        set.addListener(new Transition.TransitionListenerAdapter(){
            @Override
            public void onTransitionEnd(Transition transition) {
                super.onTransitionEnd(transition);
                caption.setTranslationX(getTextPadding());
                caption.setRotation(0);
                caption.setVerticalText(true);
                caption.requestLayout();

            }
        });
        TransitionManager.beginDelayedTransition(parent,set);
        foldStuff();
        caption.setTranslationX(-caption.getWidth()/8+getTextPadding());
    }

    private void foldStuff(){
        caption.setTextSize(TypedValue.COMPLEX_UNIT_PX,caption.getTextSize()/2f);
        caption.setTextColor(Color.WHITE);
        ConstraintLayout.LayoutParams params=getParams();
        params.rightToRight= ConstraintLayout.LayoutParams.UNSET;
        params.verticalBias=0.5f;
        caption.setLayoutParams(params);
    }

    private float getTextPadding(){
        return getResources().getDimension(R.dimen.folded_label_padding)/2.1f;
    }
}
