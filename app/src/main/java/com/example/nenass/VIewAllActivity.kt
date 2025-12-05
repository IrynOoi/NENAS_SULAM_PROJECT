////ViewAllActivity.kt
//package com.example.nenass
//
//public class ViewAllActivity extends AppCompatActivity {
//    FirebaseFirestore firestore;
//    RecyclerView recyclerView;
//    ViewAllAdapter viewAllAdapter;
//    List<ViewAllModel> viewAllModelList;
//    Toolbar toolbar;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); setContentView(R.layout.activity_view_all);
//        toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        firestore = Firebase Firestore.getInstance(); String type = getIntent().getStringExtra( name: "type"); recyclerView = findViewById(R.id.view_all_rec);
//        recyclerView.setLayoutManager(new LinearLayoutManager(context: this));
//        viewAllModelList = new ArrayList<>();
//        viewAllAdapter = new ViewAllAdapter(context: this, viewAllModelList);