final LinearLayout[] linearLayouts = new LinearLayout[2];
linearLayouts[0] = findViewById(R.id.slot1);
linearLayouts[1] = findViewById(R.id.slot2);

        SystemUI.setCornerRadius(HomeActivity.this, linearLayouts[0], ColorStateList.valueOf(0xFF202226), 300, ColorStateList.valueOf(0xFF2A2B2F), 2, 0, false);
        SystemUI.setCornerRadius(HomeActivity.this, linearLayouts[1], ColorStateList.valueOf(0xFF202226), 300, ColorStateList.valueOf(0xFF2A2B2F), 2, 0, false);

        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (LinearLayout linearLayout : linearLayouts) {
//                    if (linearLayout == view) {
//                        SystemUI.setCornerRadius(HomeActivity.this, linearLayout, ColorStateList.valueOf(0xFF2A2B2F), 300, ColorStateList.valueOf(0xFF2A2B2F), 2, 0, false);
//                    } else {
//                        SystemUI.setCornerRadius(HomeActivity.this, linearLayout, ColorStateList.valueOf(0xFF202226), 300, ColorStateList.valueOf(0xFF2A2B2F), 2, 0, false);
//                    }
if (view == linearLayouts[0]) {
intent.setClass(getApplicationContext(), OfflineActivity.class);
startActivity(intent);
}
}
}
};
for (LinearLayout linearLayout : linearLayouts) {
linearLayout.setOnClickListener(clickListener);
}