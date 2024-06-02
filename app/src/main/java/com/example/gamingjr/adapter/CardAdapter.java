package com.example.gamingjr.adapter;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.gamingjr.R;
import com.example.gamingjr.model.Card;
import com.example.gamingjr.niveles.Nivel1Activity;

import java.util.List;

public class CardAdapter extends BaseAdapter {
    private Context context;
    private List<Card> cards;
    private Card firstSelectedCard = null;
    private Card secondSelectedCard = null;
    private Handler handler = new Handler();
    private MediaPlayer mediaPlayer;
    private OnGameEndListener gameEndListener;
    private int cartasReveladas = 0;

    public CardAdapter(Context context, List<Card> cards) {
        this.context = context;
        this.cards = cards;
    }

    // Método para establecer el listener
    public void setOnGameEndListener(OnGameEndListener listener) {
        this.gameEndListener = listener;
    }

    @Override
    public int getCount() {
        return cards.size();
    }

    @Override
    public Object getItem(int position) {
        return cards.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.card_item, parent, false);
        }

        ImageView imageCovered = convertView.findViewById(R.id.imageCovered);
        ImageView imageRevealed = convertView.findViewById(R.id.imageRevealed);
        Card card = cards.get(position);

        if (card.isRevealed()) {
            imageRevealed.setVisibility(View.VISIBLE);
            imageRevealed.setImageResource(card.getImageResId());
            imageCovered.setVisibility(View.GONE);
        } else {
            imageRevealed.setVisibility(View.GONE);
            imageCovered.setVisibility(View.VISIBLE);
        }

        convertView.setOnClickListener(v -> {
            if (card.isRevealed() || secondSelectedCard != null) {
                return; // No hacer nada si la carta ya está revelada o si ya se seleccionaron dos cartas
            }

            card.setRevealed(true);
            notifyDataSetChanged();
            playSound(card.getAudioResId());

            // Incrementar el contador de cartas reveladas
            cartasReveladas++;
            gameEndListener.getIntentos(cartasReveladas);

            if (firstSelectedCard == null) {
                firstSelectedCard = card;
            } else {
                secondSelectedCard = card;
                handler.postDelayed(this::checkMatch, 1000);
            }
        });
        checkGameEnd();
        return convertView;
    }

    private void playSound(int audioResId) {
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
        mediaPlayer = MediaPlayer.create(context, audioResId);
        mediaPlayer.start();
    }

    private void checkMatch() {
        if (firstSelectedCard.getId() == secondSelectedCard.getId()) {
            // Las cartas coinciden, se mantienen reveladas
            firstSelectedCard = null;
            secondSelectedCard = null;
            checkGameEnd();
        } else {
            // Las cartas no coinciden, se vuelven a tapar
            firstSelectedCard.setRevealed(false);
            secondSelectedCard.setRevealed(false);
            firstSelectedCard = null;
            secondSelectedCard = null;
            notifyDataSetChanged();
        }
    }

    private void checkGameEnd() {
        boolean allRevealed = true;
        for (Card card : cards) {
            if (!card.isRevealed()) {
                allRevealed = false;
                break;
            }
        }

        if (allRevealed && gameEndListener != null) {
            gameEndListener.onGameEnd(cartasReveladas);
            ((Nivel1Activity)context).stopBackgroundMusic();
        }
    }


    public interface OnGameEndListener {
        void onGameEnd(int cartasReveladas);
        void getIntentos(int cartasReveladas);
    }
}

