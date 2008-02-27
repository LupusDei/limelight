require File.expand_path(File.dirname(__FILE__) + "/spec_helper")
require 'limelight/producer'

describe Limelight::Producer do
  
  before(:each) do
    @producer = Limelight::Producer.new("/tmp")
    @loader = @producer.loader
  end
  
  it "should have loader on creation" do
    @producer.loader.current_dir.should == "/tmp"
  end
  
  it "should load blocks" do
    @loader.should_receive(:load).with("./blocks.rb").and_return("child :id => 321")
    
    page = @producer.load_blocks(".")
    page.children.size.should == 1
    page.children[0].class_name.should == "child"
    page.children[0].id.should == 321
  end
  
  it "should load styles" do
    @loader.should_receive(:load).with("./styles.rb").and_return("alpha { width 100 }")
    
    styles = @producer.load_styles(".")
    styles.size.should == 1
    styles["alpha"].width.should == "100"
  end
  
  it "should format block errors well" do
    @loader.should_receive(:load).with("./blocks.rb").and_return("one\n+\nthree")
    
    begin
      result = @producer.load_blocks(".")
      result.should == nil # should never execute
    rescue Limelight::BuildException => e
      e.line_number.should == 3
      e.filename.should == "./blocks.rb"
      e.message.should == "./blocks.rb:3: undefined method `+@' for Block[id: , class_name: ]:Limelight::Page\n\t1: one\n\t2: +\n\t3: three\n"
    end
  end
  
  it "should format styles errors well" do
    @loader.should_receive(:load).with("./styles.rb").and_return("one {}\ntwo {}\n-\nthree {}")
    
    begin
      result = @producer.load_styles(".")
      result.should == nil # should never execute
    rescue Limelight::BuildException => e
      e.line_number.should == 4
      e.filename.should == "./styles.rb"
      e.message[0..73].should == "./styles.rb:4: undefined method `-@' for #<Java::LimelightUi::FlatStyle:0x"
      e.message.split("\n")[1].should == "\t1: one {}"
      e.message.split("\n")[2].should == "\t2: two {}"
      e.message.split("\n")[3].should == "\t3: -"
      e.message.split("\n")[4].should == "\t4: three {}"
    end
  end
  
  it "should load a book when index.rb exists" do
    @loader.should_receive(:exists?).with("index.rb").and_return true
    @producer.should_receive(:open_book).and_return(make_mock("book", :default_page => "abc"))
    @producer.should_receive(:open_page).with("abc")
    
    @producer.open
  end
  
  it "should load a page when index.rb doesn't exists" do
    @loader.should_receive(:exists?).with("index.rb").and_return false
    @producer.should_not_receive(:open_book)
    @producer.should_receive(:open_page).with(".")
    
    @producer.open
  end
  
  
  
end
