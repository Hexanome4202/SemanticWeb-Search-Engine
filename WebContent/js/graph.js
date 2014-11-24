(function chart1() {
  var width = 960,
      height = 500;

  var color = d3.scale.category20();

  var fisheye = d3.fisheye.circular()
      .radius(120);

  var force = d3.layout.force()
      .charge(-240)
      .linkDistance(40)
      .size([width, height]);

  var svg = d3.select("#chart1").append("svg")
      .attr("width", width)
      .attr("height", height);

  svg.append("rect")
      .attr("class", "background")
      .attr("width", width)
      .attr("height", height);

  d3.json("miserables.json", function(data) {
    var n = data.nodes.length;

    force.nodes(data.nodes).links(data.links);

    // Initialize the positions deterministically, for better results.
    data.nodes.forEach(function(d, i) { d.x = d.y = width / n * i; });

    // Run the layout a fixed number of times.
    // The ideal number of times scales with graph complexity.
    // Of course, don't run too long—you'll hang the page!
    force.start();
    for (var i = n; i > 0; --i) force.tick();
    force.stop();

    // Center the nodes in the middle.
    var ox = 0, oy = 0;
    data.nodes.forEach(function(d) { ox += d.x, oy += d.y; });
    ox = ox / n - width / 2, oy = oy / n - height / 2;
    data.nodes.forEach(function(d) { d.x -= ox, d.y -= oy; });

    var link = svg.selectAll(".link")
        .data(data.links)
      .enter().append("line")
        .attr("class", "link")
        .attr("x1", function(d) { return d.source.x; })
        .attr("y1", function(d) { return d.source.y; })
        .attr("x2", function(d) { return d.target.x; })
        .attr("y2", function(d) { return d.target.y; })
        .style("stroke-width", function(d) { return Math.sqrt(d.value); });

    var node = svg.selectAll(".node")
        .data(data.nodes)
      .enter().append("circle")
        .attr("class", "node")
        .attr("cx", function(d) { return d.x; })
        .attr("cy", function(d) { return d.y; })
        .attr("r", 4.5)
        .style("fill", function(d) { return color(d.group); })
        .call(force.drag);

    svg.on("mousemove", function() {
      fisheye.focus(d3.mouse(this));

      node.each(function(d) { d.fisheye = fisheye(d); })
          .attr("cx", function(d) { return d.fisheye.x; })
          .attr("cy", function(d) { return d.fisheye.y; })
          .attr("r", function(d) { return d.fisheye.z * 4.5; });

      link.attr("x1", function(d) { return d.source.fisheye.x; })
          .attr("y1", function(d) { return d.source.fisheye.y; })
          .attr("x2", function(d) { return d.target.fisheye.x; })
          .attr("y2", function(d) { return d.target.fisheye.y; });
    });
  });
})();

(function chart2() {
  var width = 960,
      height = 180,
      xStepsBig = d3.range(10, width, 16),
      yStepsBig = d3.range(10, height, 16),
      xStepsSmall = d3.range(0, width + 6, 6),
      yStepsSmall = d3.range(0, height + 6, 6);

  var fisheye = d3.fisheye.circular()
      .focus([360, 90])
      .radius(100);

  var line = d3.svg.line();

  var svg = d3.select("#chart2").append("svg")
      .attr("width", width)
      .attr("height", height)
    .append("g")
      .attr("transform", "translate(-.5,-.5)");

  svg.append("rect")
      .attr("class", "background")
      .attr("width", width)
      .attr("height", height);

  svg.selectAll(".x")
      .data(xStepsBig)
    .enter().append("path")
      .attr("class", "x")
      .datum(function(x) { return yStepsSmall.map(function(y) { return [x, y]; }); });

  svg.selectAll(".y")
      .data(yStepsBig)
    .enter().append("path")
      .attr("class", "y")
      .datum(function(y) { return xStepsSmall.map(function(x) { return [x, y]; }); });

  var path = svg.selectAll("path")
      .attr("d", fishline);

  svg.on("mousemove", function() {
    fisheye.focus(d3.mouse(this));
    path.attr("d", fishline);
  });

  function fishline(d) {
    return line(d.map(function(d) {
      d = fisheye({x: d[0], y: d[1]});
      return [d.x, d.y];
    }));
  }
})();

(function chart3() {
  var width = 960,
      height = 180,
      xSteps = d3.range(10, width, 16),
      ySteps = d3.range(10, height, 16);

  var xFisheye = d3.fisheye.scale(d3.scale.identity).domain([0, width]).focus(360),
      yFisheye = d3.fisheye.scale(d3.scale.identity).domain([0, height]).focus(90);

  var svg = d3.select("#chart3").append("svg")
      .attr("width", width)
      .attr("height", height)
    .append("g")
      .attr("transform", "translate(-.5,-.5)");

  svg.append("rect")
      .attr("class", "background")
      .attr("width", width)
      .attr("height", height);

  var xLine = svg.selectAll(".x")
      .data(xSteps)
    .enter().append("line")
      .attr("class", "x")
      .attr("y2", height);

  var yLine = svg.selectAll(".y")
      .data(ySteps)
    .enter().append("line")
      .attr("class", "y")
      .attr("x2", width);

  redraw();

  svg.on("mousemove", function() {
    var mouse = d3.mouse(this);
    xFisheye.focus(mouse[0]);
    yFisheye.focus(mouse[1]);
    redraw();
  });

  function redraw() {
    xLine.attr("x1", xFisheye).attr("x2", xFisheye);
    yLine.attr("y1", yFisheye).attr("y2", yFisheye);
  }
})();

(function chart4() {

  // Various accessors that specify the four dimensions of data to visualize.
  function x(d) { return d.income; }
  function y(d) { return d.lifeExpectancy; }
  function radius(d) { return d.population; }
  function color(d) { return d.region; }

  // Chart dimensions.
  var margin = {top: 5.5, right: 19.5, bottom: 12.5, left: 39.5},
      width = 960,
      height = 500 - margin.top - margin.bottom;

  // Various scales and distortions.
  var xScale = d3.fisheye.scale(d3.scale.log).domain([300, 1e5]).range([0, width]),
      yScale = d3.fisheye.scale(d3.scale.linear).domain([20, 90]).range([height, 0]),
      radiusScale = d3.scale.sqrt().domain([0, 5e8]).range([0, 40]),
      colorScale = d3.scale.category10().domain(["Sub-Saharan Africa", "South Asia", "Middle East & North Africa", "America", "Europe & Central Asia", "East Asia & Pacific"]);

  // The x & y axes.
  var xAxis = d3.svg.axis().orient("bottom").scale(xScale).tickFormat(d3.format(",d")).tickSize(-height),
      yAxis = d3.svg.axis().scale(yScale).orient("left").tickSize(-width);

  // Create the SVG container and set the origin.
  var svg = d3.select("#chart4").append("svg")
      .attr("width", width + margin.left + margin.right)
      .attr("height", height + margin.top + margin.bottom)
    .append("g")
      .attr("transform", "translate(" + margin.left + "," + margin.top + ")");

  // Add a background rect for mousemove.
  svg.append("rect")
      .attr("class", "background")
      .attr("width", width)
      .attr("height", height);

  // Add the x-axis.
  svg.append("g")
      .attr("class", "x axis")
      .attr("transform", "translate(0," + height + ")")
      .call(xAxis);

  // Add the y-axis.
  svg.append("g")
      .attr("class", "y axis")
      .call(yAxis);

  // Add an x-axis label.
  svg.append("text")
      .attr("class", "x label")
      .attr("text-anchor", "end")
      .attr("x", width - 6)
      .attr("y", height - 6)
      .text("income per capita, inflation-adjusted (dollars)");

  // Add a y-axis label.
  svg.append("text")
      .attr("class", "y label")
      .attr("text-anchor", "end")
      .attr("x", -6)
      .attr("y", 6)
      .attr("dy", ".75em")
      .attr("transform", "rotate(-90)")
      .text("life expectancy (years)");

  // Load the data.
  d3.json("nations.json", function(nations) {

    // Add a dot per nation. Initialize the data at 1800, and set the colors.
    var dot = svg.append("g")
        .attr("class", "dots")
      .selectAll(".dot")
        .data(nations)
      .enter().append("circle")
        .attr("class", "dot")
        .style("fill", function(d) { return colorScale(color(d)); })
        .call(position)
        .sort(function(a, b) { return radius(b) - radius(a); });

    // Add a title.
    dot.append("title")
        .text(function(d) { return d.name; });

    // Positions the dots based on data.
    function position(dot) {
      dot .attr("cx", function(d) { return xScale(x(d)); })
          .attr("cy", function(d) { return yScale(y(d)); })
          .attr("r", function(d) { return radiusScale(radius(d)); });
    }

    svg.on("mousemove", function() {
      var mouse = d3.mouse(this);
      xScale.distortion(2.5).focus(mouse[0]);
      yScale.distortion(2.5).focus(mouse[1]);

      dot.call(position);
      svg.select(".x.axis").call(xAxis);
      svg.select(".y.axis").call(yAxis);
    });
  });
})();
