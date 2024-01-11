package com.fsd08.MediLink.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fsd08.MediLink.entity.Appointment;
import com.fsd08.MediLink.entity.Doctor_schedule;
import com.fsd08.MediLink.entity.Appointment.Appointment_Status;
import com.fsd08.MediLink.entity.User;
import com.fsd08.MediLink.repository.AppointmentRepository;
import com.fsd08.MediLink.repository.Doctor_scheduleRepository;
import com.fsd08.MediLink.repository.UserRepository;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.param.PaymentIntentListParams;
import com.stripe.param.PaymentIntentSearchParams;
import com.stripe.param.ChargeUpdateParams.FraudDetails.UserReport;

import java.math.BigDecimal;
import java.util.List;

@Service
public class PaymentService {
    @Value("${STRIPE_API_KEY}")
    private String stripeApiKey;

    private AppointmentRepository appointmentRepository;
    private Doctor_scheduleRepository doctor_scheduleRepository;
    private UserRepository userRepository;

    public PaymentService(AppointmentRepository appointmentRepository,
            Doctor_scheduleRepository doctor_scheduleRepository, UserRepository userRepository) {
        this.appointmentRepository = appointmentRepository;
        this.doctor_scheduleRepository = doctor_scheduleRepository;
        this.userRepository = userRepository;
    }

    public int getPrice(int appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId).orElse(null);
        if (appointment != null) {
            Doctor_schedule doctor_schedule = doctor_scheduleRepository.findById(appointment.getDoctor_schedule_id())
                    .orElse(null);
            if (doctor_schedule != null) {
                User doctor = userRepository.findById(doctor_schedule.getDoctor_id());
                if (doctor != null) {
                    BigDecimal price = doctor.getDefault_price();
                    if (price != null) {
                        return price.multiply(new BigDecimal(100)).intValue();
                    }
                }
            }
        }
        return 0;
    }

    private void setAppointmentStatus(int appointmentId, Appointment_Status status) {
        Appointment appointment = appointmentRepository.findById(appointmentId).orElse(null);
        if (appointment != null) {
            try {
                appointment.setStatus(status);
                appointmentRepository.save(appointment);
            } catch (Exception e) {

            }

        }
    }

    public PaymentIntent createPaymentIntent(long amount, String currency, String description, int appointmentId)
            throws StripeException {
        Stripe.apiKey = stripeApiKey;
        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(amount)
                .setCurrency(currency)
                .putMetadata("appointmentId", Integer.toString(appointmentId))
                .setAutomaticPaymentMethods(
                        PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
                                .setEnabled(true)
                                .build())
                .build();

        try {
            PaymentIntent paymentIntent = PaymentIntent.create(params);
            return paymentIntent;
        } catch (StripeException e) {
            e.printStackTrace();
            return null;
        }

    }

    public List<PaymentIntent> getPaymentIntents(int appointmentId) throws StripeException {
        Stripe.apiKey = stripeApiKey;
        String query = String.format("metadata['appointmentId']:'%s'", appointmentId);
        PaymentIntentSearchParams params = PaymentIntentSearchParams.builder().setQuery(query).build();
        List<PaymentIntent> paymentIntents = PaymentIntent.search(params).getData();
        return paymentIntents;
    }

    public int getPaymentIntentsSucceededCount(int appointmentId) {
        try {
            List<PaymentIntent> paymentIntents = getPaymentIntents(appointmentId);
            int count = (int) paymentIntents.stream().filter(p -> p.getStatus().equals("succeeded")).count();
            if (count > 0) {
                setAppointmentStatus(appointmentId, Appointment_Status.CONFIRMED);
            }
            return count;
        } catch (StripeException e) {
            return 0;
        }
    }

}
