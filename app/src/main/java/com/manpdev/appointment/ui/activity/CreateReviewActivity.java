package com.manpdev.appointment.ui.activity;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.manpdev.appointment.R;
import com.manpdev.appointment.data.model.AppointmentModel;
import com.manpdev.appointment.data.model.ReviewModel;
import com.manpdev.appointment.databinding.ActivityCreateReviewBinding;
import com.manpdev.appointment.ui.utils.KeyboardUtil;

import java.util.Date;

public class CreateReviewActivity extends AppCompatActivity {

    public static final String APPOINTMENT_MODEL_EXTRA = "::appointment_model_extra";

    private ActivityCreateReviewBinding mViewBinding;
    private AppointmentModel mAppointment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewBinding = DataBindingUtil.setContentView(this, R.layout.activity_create_review);
        mAppointment = getIntent().getParcelableExtra(APPOINTMENT_MODEL_EXTRA);


        mViewBinding.btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(Activity.RESULT_CANCELED);
                finish();
            }
        });

        mViewBinding.bnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createReview();
                KeyboardUtil.hideKeyboard(view);
                finish();
            }
        });
    }

    private void createReview() {
        if(mAppointment == null)
            return;

        if(TextUtils.isEmpty(mViewBinding.etReview.getText())) {
            Toast.makeText(this, R.string.invalid_review, Toast.LENGTH_LONG).show();
            return;
        }

        ReviewModel model = new ReviewModel();
        model.setAuthor(mViewBinding.etAuthor.getText().toString());
        model.setDate(new Date());
        model.setRating(mViewBinding.rbRatings.getRating());
        model.setReview(mViewBinding.etReview.getText().toString());
        model.setUid(mAppointment.getPid());

        Intent data = new Intent();
        data.putExtra(ClientAppointmentListActivity.NEW_REVIEW_EXTRA, model);
        setResult(Activity.RESULT_OK, data);
    }
}
