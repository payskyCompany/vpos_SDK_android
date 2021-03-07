package com.paysky.upg.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.paysky.upg.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.paysky.upg.data.network.model.response.DateTransactions;

import static com.paysky.upg.fragment.TransactionsLoadingFragment.FROM_WHERE;

public class AnimationVideoTransactionsFragment extends BaseFragment implements Player.EventListener {

    DateTransactions dateTransactions;

    @BindView(R.id.vp_animated_transaction)
    PlayerView viewPlayer;

    DataSource.Factory mediaDataSourceFactory;
    ExoPlayer exoPlayer;

    String cardType = "";
    String cardNo = "";
    MediaSource videoSource;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View root = inflater.inflate(R.layout.fragment_animation_video_transaction, container, false);
        ButterKnife.bind(this, root);
        if (getArguments() != null && getArguments().getSerializable(FROM_WHERE) != null) {
            dateTransactions = (DateTransactions) getArguments().getSerializable(FROM_WHERE);
            if (dateTransactions != null && dateTransactions.getCardType() != null) {
                cardType = dateTransactions.getCardType();
            }

        }

        initVideoPlayer();
        return root;
    }

    private void initVideoPlayer() {
        exoPlayer = ExoPlayerFactory.newSimpleInstance(getContext());
        String userAgent = Util.getUserAgent(getContext(), getContext().getString(R.string.app_name));
        mediaDataSourceFactory = new DefaultDataSourceFactory(getContext(), userAgent);

        if (cardType.equals("VISA")) {
            videoSource = new ExtractorMediaSource
                    .Factory(new DefaultDataSourceFactory(getContext(), userAgent))
                    .setExtractorsFactory(new DefaultExtractorsFactory())
                    .createMediaSource(Uri.parse("asset:///visa_animation_transaction.mp4"));
        } else if (cardType.equals("MASTERCARD")) {
            videoSource = new ExtractorMediaSource
                    .Factory(new DefaultDataSourceFactory(getContext(), userAgent))
                    .setExtractorsFactory(new DefaultExtractorsFactory())
                    .createMediaSource(Uri.parse("asset:///visa_animation_transaction.mp4"));
        }
        viewPlayer.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT);
        exoPlayer.prepare(videoSource);
        viewPlayer.setPlayer(exoPlayer);
        viewPlayer.hideController();
        viewPlayer.setUseController(false);
        viewPlayer.setShutterBackgroundColor(android.R.color.white);
        viewPlayer.requestFocus();
        exoPlayer.setPlayWhenReady(true);
        exoPlayer.addListener(this);
        viewPlayer.setControllerVisibilityListener(visibility -> {
            if (visibility == 0) {
                viewPlayer.hideController();
            }
        });

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if (playbackState == Player.STATE_ENDED) {
            addNewFragmentNullBackStack(ReceiptWithTransFragment.newInstance(dateTransactions), "ReceiptWithTransFragment");
        }
    }

    public static AnimationVideoTransactionsFragment newInstance(DateTransactions dateTransactions) {
        AnimationVideoTransactionsFragment fragment = new AnimationVideoTransactionsFragment();
        Bundle args = new Bundle();
        args.putSerializable(FROM_WHERE, dateTransactions);
        fragment.setArguments(args);
        return fragment;
    }

}