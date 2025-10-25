package com.midterm.dogapp.viewmodel;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.midterm.dogapp.R;
import com.midterm.dogapp.databinding.DogsItemBinding;
import com.midterm.dogapp.model.DogBreed;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class DogAdapter extends RecyclerView.Adapter<DogAdapter.ViewHolder> implements Filterable {

    private ArrayList<DogBreed> dogBreeds;
    private ArrayList<DogBreed> dogBreedsCopy;

    public DogAdapter(ArrayList<DogBreed> dogBreeds) {
        this.dogBreeds = dogBreeds;
        this.dogBreedsCopy = dogBreeds;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dogs_item, parent, false);
        DogsItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.dogs_item, parent, false);

        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        holder.tvName.setText(dogBreeds.get(position).getName());
//        holder.tvOrigin.setText(dogBreeds.get(position).getOrigin());
        holder.binding.setDog(dogBreeds.get(position));
        Picasso.get().load(dogBreeds.get(position).getUrl()).into(holder.binding.ivAvatar);
    }

    @Override
    public int getItemCount() {
        return dogBreeds.size();
    }

    @Override

    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String input = charSequence.toString().toLowerCase();
                List<DogBreed> filteredList = new ArrayList<>();

                if (input.isEmpty()) {
                    filteredList.addAll(dogBreedsCopy); // nếu không nhập gì thì trả lại toàn bộ
                } else {
                    for (DogBreed dog : dogBreedsCopy) {
                        if (dog.getName().toLowerCase().contains(input)) {
                            filteredList.add(dog);
                        }
                    }
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults filterResults) {
                dogBreeds= new ArrayList<>();
                dogBreeds.addAll((List)filterResults.values);
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public DogsItemBinding binding;
        private boolean isLiked = false;
        public ViewHolder(DogsItemBinding itemBinding) {
            super(itemBinding.getRoot());
            this.binding = itemBinding;
//            tvName = view.findViewById(R.id.tv_name);
//            tvOrigin = view.findViewById(R.id.tv_origin);
//            ivAvatar = view.findViewById(R.id.iv_avatar);

            binding.ivHeart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!isLiked) {
                        // nếu chưa thích, chuyển sang đỏ
                        binding.ivHeart.setColorFilter(
                                v.getContext().getResources().getColor(android.R.color.holo_red_light),
                                android.graphics.PorterDuff.Mode.SRC_IN
                        );
                        isLiked = true;
                    } else {
                        // nếu đã thích, trở về màu ban đầu
                        binding.ivHeart.clearColorFilter();
                        isLiked = false;
                    }
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DogBreed dog = dogBreeds.get(getAdapterPosition());
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("dogBreed", dog);
                    Navigation.findNavController(v).navigate(R.id.detailFragment, bundle);
                }
            });

            itemView.setOnTouchListener(
                    new OnSwipeTouchListener(binding.ivAvatar.getContext()) {
                        @Override
                        public void onSwipeLeft() {
                            if(binding.layout1.getVisibility() == View.GONE) {
                                binding.layout1.setVisibility(View.VISIBLE);
                                binding.layout2.setVisibility(View.GONE);
                            }
                            else {
                                binding.layout1.setVisibility(View.GONE);
                                binding.layout2.setVisibility(View.VISIBLE);
                            }
                            super.onSwipeLeft();
                        }
                    }
            );


        }

    }

//    public class OnSwipeTouchListener implements View.OnTouchListener {
//
//        private final GestureDetector gestureDetector = new GestureDetector(new GestureListener());
//
//        public boolean onTouch(final View view, final MotionEvent motionEvent) {
//            return gestureDetector.onTouchEvent(motionEvent);
//        }
//
//        private final class GestureListener extends GestureDetector.SimpleOnGestureListener {
//
//            private static final int SWIPE_THRESHOLD = 100;
//            private static final int SWIPE_VELOCITY_THRESHOLD = 100;
//
//            @Override
//            public boolean onDown(MotionEvent e) {
//                return true;
//            }
//
//            // Determines the fling velocity and then fires the appropriate swipe event accordingly
//            @Override
//            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
//                boolean result = false;
//                try {
//                    float diffY = e2.getY() - e1.getY();
//                    float diffX = e2.getX() - e1.getX();
//                    if (Math.abs(diffX) > Math.abs(diffY)) {
//                        if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
//                            if (diffX > 0) {
//                                onSwipeRight();
//                            } else {
//                                onSwipeLeft();
//                            }
//                        }
//                    } else {
//                        if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
//                            if (diffY > 0) {
//                                onSwipeDown();
//                            } else {
//                                onSwipeUp();
//                            }
//                        }
//                    }
//                } catch (Exception exception) {
//                    exception.printStackTrace();
//                }
//                return result;
//            }
//        }
//
//        public void onSwipeRight() {
//        }
//
//        public void onSwipeLeft() {
//        }
//
//        public void onSwipeUp() {
//        }
//
//        public void onSwipeDown() {
//        }
//    }

    public class OnSwipeTouchListener implements View.OnTouchListener {

        private final GestureDetector gestureDetector;

        public OnSwipeTouchListener(Context context) {
            gestureDetector = new GestureDetector(context, new GestureListener());
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            return gestureDetector.onTouchEvent(event);
        }


        private final class GestureListener extends GestureDetector.SimpleOnGestureListener {

            private static final int SWIPE_THRESHOLD = 100;
            private static final int SWIPE_VELOCITY_THRESHOLD = 100;

            @Override
            public boolean onDown(MotionEvent e) {
                return false;
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                float diffY = e2.getY() - e1.getY();
                float diffX = e2.getX() - e1.getX();

                if (Math.abs(diffX) > Math.abs(diffY)) {
                    if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffX > 0) {
                            onSwipeRight();
                        } else {
                            onSwipeLeft();
                        }
                        return true;
                    }
                } else {
                    if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffY > 0) {
                            onSwipeDown();
                        } else {
                            onSwipeUp();
                        }
                        return true;
                    }
                }
                return false;
            }
        }

        public void onSwipeRight() {}
        public void onSwipeLeft() {}
        public void onSwipeUp() {}
        public void onSwipeDown() {}
    }

}
