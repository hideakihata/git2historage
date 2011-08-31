package jp.ac.osaka_u.ist.sel.metricstool.main.ast.token;


public abstract class BlockNameToken extends AstTokenAdapter {

	public BlockNameToken(String text){
		super(text);
	}
	
	@Override
	public boolean isBlockName() {
		return true;
	}
	
	@Override
    public boolean isBlockDefinition() {
        return true;
    }
	
	@Override
	public boolean isStatement() {
	    return true;
	}
	
	public static BlockNameToken IF_BLOCK = new BlockNameToken("if"){

		@Override
		public boolean isIf() {
		    return true;
		}

	};
	
	public static BlockNameToken ELSE_BLOCK = new BlockNameToken("else"){

		@Override
		public boolean isElse() {
		    return true;
		}
		
	};
	
	public static BlockNameToken WHILE_BLOCK = new BlockNameToken("while"){

		@Override
		public boolean isWhile() {
		    return true;
		}

	};
	
	public static BlockNameToken DO_BLOCK = new BlockNameToken("do"){

		@Override
		public boolean isDo() {
		    return true;
		}

	};
	
	public static BlockNameToken FOR_BLOCK = new BlockNameToken("for"){

		@Override
		public boolean isFor() {
		    return true;
		}

	};
	
	public static BlockNameToken FOREACH_BLOCK = new BlockNameToken("foreach"){

	    @Override
	    public boolean isForeach() {
	        return true;
	   }

	};


	public static BlockNameToken TRY_BLOCK = new BlockNameToken("try"){

		@Override
		public boolean isTry() {
		    return true;
		}

	};
	
	public static BlockNameToken CATCH_BLOCK = new BlockNameToken("catch"){

		@Override
		public boolean isCatch() {
		    return true;
		}

	};
	
	public static BlockNameToken FINALLY_BLOCK = new BlockNameToken("finally"){

		@Override
		public boolean isFinally() {
		    return true;
		}

	};
	
	public static BlockNameToken SWITCH_BLOCK = new BlockNameToken("switch"){

		@Override
		public boolean isSwitch() {
		    return true;
		}

	};
	
	public static BlockNameToken CASE_GROUP_DEFINITION = new BlockNameToken("CASE_GROUP") {
	    
	    @Override
	    public boolean isCaseGroupDefinition() {
	        return true;
	    }
	    
	};
	
	public static BlockNameToken CASE_ENTRY = new BlockNameToken("case"){

		@Override
		public boolean isCase() {
		    return true;
		}
		
		@Override
		public boolean isEntryDefinition() {
		    return true;
		}	    

	};
	
	public static BlockNameToken DEFAULT_ENTRY = new BlockNameToken("default"){

		@Override
		public boolean isDefault() {
		    return true;
		}

		@Override
        public boolean isEntryDefinition() {
            return true;
        }
	};
	
}
